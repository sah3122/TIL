package dean.spring.sandbox.member

import org.springframework.data.jpa.repository.JpaRepository
import java.util.stream.Stream

interface MemberRepository : JpaRepository<Member, Long> {

    fun streamAllBy(): Stream<Member>
}
