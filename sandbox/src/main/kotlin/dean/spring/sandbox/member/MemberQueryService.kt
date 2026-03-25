package dean.spring.sandbox.member

import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.atomic.AtomicInteger
import java.util.stream.Stream

@Service
@Transactional(readOnly = true)
class MemberQueryService(private val memberRepository: MemberRepository) {

    fun <R> withMemberStream(action: (Stream<Member>) -> R): R {
        val counter = AtomicInteger(0)
        return memberRepository.streamAllBy()
            .peek {
                val count = counter.incrementAndGet()
                if (count % FETCH_SIZE == 0) {
                    log.info("Fetched {} members (chunk #{})", count, count / FETCH_SIZE)
                }
            }
            .use(action)
    }

    fun findPage(page: Int, size: Int): List<Member> =
        memberRepository.findAll(PageRequest.of(page, size, Sort.by("id"))).content

    companion object {
        private val log = LoggerFactory.getLogger(MemberQueryService::class.java)
        private const val FETCH_SIZE = 1000
    }
}
