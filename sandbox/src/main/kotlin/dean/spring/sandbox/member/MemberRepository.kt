package dean.spring.sandbox.member

import jakarta.persistence.QueryHint
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.QueryHints
import java.util.stream.Stream

interface MemberRepository : JpaRepository<Member, Long> {

    @QueryHints(value = [QueryHint(name = "org.hibernate.fetchSize", value = "1000")])
    fun streamAllBy(): Stream<Member>
}
