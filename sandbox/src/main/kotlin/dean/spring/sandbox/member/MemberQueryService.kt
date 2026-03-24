package dean.spring.sandbox.member

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberQueryService(private val memberRepository: MemberRepository) {

    fun findPage(page: Int, size: Int): List<Member> =
        memberRepository.findAll(PageRequest.of(page, size, Sort.by("id"))).content
}
