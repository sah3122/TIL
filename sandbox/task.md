# FastExcel 스트리밍 Excel 다운로드 — Task

## 1. 프로젝트 설정

- [x] `build.gradle.kts`에 FastExcel 의존성 추가
  ```kotlin
  implementation("org.dhatim:fastexcel:0.19.0")
  ```
- [x] `build.gradle.kts`에 H2 테스트 의존성 추가
  ```kotlin
  testRuntimeOnly("com.h2database:h2")
  ```
- [x] `application.properties`에 DataSource 설정 추가 (`useCursorFetch=true`, `defaultFetchSize=1000`)
- [x] `application.properties`에 JPA 설정 추가 (`open-in-view=false`, dialect 등)
- [x] `application.properties`에 async timeout 설정 추가 (`spring.mvc.async.request-timeout=300000`)

---

## 2. Entity

- [x] `Member.kt` 생성 (`dean.spring.sandbox.member` 패키지) ✅
  - 필드: `id`, `name`, `email`, `phoneNumber`, `status`, `birthDate`, `createdAt`
  - `@Entity`, `@Table(name = "member")`, `@GeneratedValue(IDENTITY)` 적용

---

## 3. Repository

- [x] `MemberRepository.kt` 생성 ✅
  - `JpaRepository<Member, Long>` 상속
  - 별도 메서드 불필요 (기본 `findAll(Pageable)` 활용)

---

## 4. Config

- [x] `ExcelConfig.kt` 생성 (`dean.spring.sandbox.excel` 패키지) ✅
  - `excelTaskExecutor` 빈 등록 (corePool=2, maxPool=5, prefix=`excel-export-`)
  - `WebMvcConfigurer.configureAsyncSupport()` 오버라이드 — timeout 300s, executor 연결

---

## 5. Query Service (트랜잭션 레이어)

- [ ] `MemberQueryService.kt` 생성
  - `@Transactional(readOnly = true)` 클래스 레벨 적용
  - `findPage(page: Int, size: Int): List<Member>` 구현 — `PageRequest.of(page, size, Sort.by("id"))`

---

## 6. Excel Service (스트리밍 레이어)

- [ ] `MemberExcelService.kt` 생성
  - 트랜잭션 어노테이션 없음 (데이터 조회는 `MemberQueryService`에 위임)
  - `streamMembersToExcel(): StreamingResponseBody` 구현
    - `Workbook(outputStream, ...).use { }` 로 자원 자동 해제
    - 5000건 페이지 단위 반복 루프
    - `writeHeader()`, `writeRow()` private 메서드 분리

---

## 7. Controller

- [ ] `MemberExcelController.kt` 생성
  - `GET /api/members/excel` 엔드포인트
  - `Content-Type: application/vnd.openxmlformats-officedocument.spreadsheetml.sheet` 헤더 설정
  - `Content-Disposition: attachment; filename="members_YYYYMMDD.xlsx"` 헤더 설정
  - `Content-Length` 미설정 (chunked transfer encoding 자동 적용)

---

## 8. 테스트

- [ ] `MemberExcelServiceTest.kt` 생성 (단위 테스트)
  - `MemberQueryService` Mockito 모킹
  - `StreamingResponseBody.writeTo(ByteArrayOutputStream)` 실행
  - 출력 바이트의 앞 2바이트가 PK 매직 바이트(`0x50 0x4B`) 인지 검증
- [ ] `MemberExcelControllerTest.kt` 생성 (통합 테스트)
  - `@SpringBootTest(webEnvironment = RANDOM_PORT)` + H2
  - `@BeforeEach`에서 테스트 Member 500건 삽입
  - `GET /api/members/excel` 호출 → HTTP 200, Content-Type, Content-Disposition 검증
- [ ] 수동 테스트: 로컬 MySQL 연결 후 curl 또는 브라우저로 xlsx 다운로드 확인
