package dean.spring.sandbox.member

import jakarta.persistence.QueryHint
import org.hibernate.jpa.HibernateHints
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.QueryHints
import java.util.stream.Stream

interface MemberRepository : JpaRepository<Member, Long> {
    @QueryHints(QueryHint(name = HibernateHints.HINT_FETCH_SIZE, value = "1000"))
    fun streamAllBy(): Stream<Member>
}
