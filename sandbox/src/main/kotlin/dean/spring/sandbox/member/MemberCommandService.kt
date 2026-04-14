package dean.spring.sandbox.member

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime

@Service
@Transactional
class MemberCommandService(private val memberRepository: MemberRepository) {

    fun generateDummy(count: Int): Int {
        val members = (1..count).map { i ->
            Member(
                name = "Member_$i",
                email = "member_${System.nanoTime()}_$i@dummy.com",
                phoneNumber = "010-%04d-%04d".format((1000..9999).random(), (1000..9999).random()),
                status = listOf("ACTIVE", "INACTIVE").random(),
                birthDate = LocalDate.of((1970..2000).random(), (1..12).random(), (1..28).random()),
                createdAt = LocalDateTime.now(),
            )
        }
        memberRepository.saveAll(members)
        return count
    }
}
