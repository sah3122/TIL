# FastExcel 스트리밍 Excel 다운로드 구현 플랜

## 개요

Spring Boot 4.x / JPA / MySQL 환경에서 10만 건+ 대용량 데이터를 메모리 부담 없이 Excel로 스트리밍 다운로드하는 기능을 구현한다.

- **라이브러리**: FastExcel (`org.dhatim:fastexcel:0.19.0`) — Apache POI 없이 OOXML을 OutputStream에 직접 쓰는 경량 라이브러리
- **핵심 과제**: `StreamingResponseBody` 비동기 스레드와 `@Transactional` 레이어 경계 분리

---

## 핵심 아키텍처: 페이지네이션 기반 레이어 분리

### 스레드 흐름

```
Thread A (Tomcat)
  → Controller
  → MemberExcelService.streamMembersToExcel()   ← StreamingResponseBody 람다만 반환
  → ResponseEntity 반환

Thread B (excel-export-* Executor)
  → StreamingResponseBody.writeTo(outputStream)
       └─ loop: memberQueryService.findPage(page++, 5000)
            └─ @Transactional(readOnly=true) 가 Thread B에서 자동 적용
            └─ 5000건 조회 → 트랜잭션 종료 → 커넥션 반환
       └─ writeRow() × 5000 (FastExcel → OutputStream)
       └─ workbook.finish()
```

### 핵심 원칙

`TransactionTemplate`을 수동으로 선언하지 않는다.
`StreamingResponseBody` 람다가 `@Transactional` 서비스를 페이지 단위로 반복 호출하면,
각 호출이 Thread B에서 독립적인 트랜잭션을 자동으로 열고 닫는다.

---

## 레이어 구조

| 레이어 | 클래스 | 책임 |
|--------|--------|------|
| Repository | `MemberRepository` | JPA 기본 쿼리 |
| Query Service | `MemberQueryService` | `@Transactional(readOnly=true)` 데이터 조회 |
| Excel Service | `MemberExcelService` | Excel 생성, StreamingResponseBody 반환 |
| Controller | `MemberExcelController` | HTTP 응답 헤더/바디 구성 |
| Config | `ExcelConfig` | TaskExecutor + AsyncSupportConfigurer |

---

## 파일 목록

```
src/main/kotlin/dean/spring/sandbox/
├── excel/
│   └── ExcelConfig.kt                   # TaskExecutor + WebMvcConfigurer
└── member/
    ├── Member.kt                         # JPA Entity
    ├── MemberRepository.kt               # JpaRepository
    ├── MemberQueryService.kt             # @Transactional 데이터 조회
    ├── MemberExcelService.kt             # StreamingResponseBody 생성
    └── MemberExcelController.kt          # GET /api/members/excel

src/main/resources/
└── application.properties               # DB/JPA/async 설정 추가

src/test/kotlin/dean/spring/sandbox/member/
├── MemberExcelServiceTest.kt            # 단위 테스트
└── MemberExcelControllerTest.kt         # 통합 테스트 (H2)
```

---

## 주요 설정 포인트

### build.gradle.kts
```kotlin
implementation("org.dhatim:fastexcel:0.19.0")
testRuntimeOnly("com.h2database:h2")  // 통합 테스트용
```

### application.properties (핵심)
```properties
# useCursorFetch=true + defaultFetchSize=1000 → MySQL 서버사이드 커서 활성화
spring.datasource.url=jdbc:mysql://localhost:3306/sandbox_db?useSSL=false&serverTimezone=Asia/Seoul&useCursorFetch=true&defaultFetchSize=1000
spring.jpa.open-in-view=false          # 스트리밍과 충돌 방지 — 반드시 false
spring.mvc.async.request-timeout=300000
```

### ExcelConfig.kt
```kotlin
@Configuration
class ExcelConfig : WebMvcConfigurer {
    @Bean("excelTaskExecutor")
    fun excelTaskExecutor() = ThreadPoolTaskExecutor().apply {
        corePoolSize = 2; maxPoolSize = 5; queueCapacity = 10
        setThreadNamePrefix("excel-export-"); initialize()
    }
    override fun configureAsyncSupport(configurer: AsyncSupportConfigurer) {
        configurer.setDefaultTimeout(300_000)
        configurer.setTaskExecutor(excelTaskExecutor())
    }
}
```

### MemberQueryService.kt
```kotlin
@Service
@Transactional(readOnly = true)
class MemberQueryService(private val memberRepository: MemberRepository) {
    fun findPage(page: Int, size: Int): List<Member> =
        memberRepository.findAll(PageRequest.of(page, size, Sort.by("id"))).content
}
```

### MemberExcelService.kt
```kotlin
@Service
class MemberExcelService(private val memberQueryService: MemberQueryService) {
    fun streamMembersToExcel(): StreamingResponseBody {
        return StreamingResponseBody { outputStream ->
            Workbook(outputStream, "SandboxApp", "1.0").use { workbook ->
                val ws = workbook.newWorksheet("Members")
                writeHeader(ws)
                var row = 1; var page = 0
                do {
                    val batch = memberQueryService.findPage(page++, PAGE_SIZE)
                    batch.forEach { writeRow(ws, row++, it) }
                } while (batch.size == PAGE_SIZE)
            }
        }
    }
    companion object { private const val PAGE_SIZE = 5_000 }
}
```

---

## 검증 방법

1. **단위 테스트**: `MemberQueryService` 모킹 → PK 매직 바이트(`0x50 0x4B`) 확인
2. **통합 테스트**: H2 + 500건 삽입 → `GET /api/members/excel` 상태코드/헤더 검증
3. **수동 테스트**: curl 또는 브라우저로 xlsx 다운로드 확인
