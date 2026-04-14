package dean.spring.sandbox.member

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/members")
class MemberController(private val memberCommandService: MemberCommandService) {

    @PostMapping("/dummy")
    fun generateDummy(
        @RequestParam(defaultValue = "100") count: Int,
    ): ResponseEntity<Map<String, Int>> {
        val inserted = memberCommandService.generateDummy(count)
        return ResponseEntity.ok(mapOf("inserted" to inserted))
    }
}
