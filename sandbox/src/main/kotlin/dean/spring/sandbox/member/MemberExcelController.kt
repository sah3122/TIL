package dean.spring.sandbox.member

import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RestController
@RequestMapping("/api/members")
class MemberExcelController(private val memberExcelService: MemberExcelService) {

    @GetMapping("/excel")
    fun downloadExcel(): ResponseEntity<StreamingResponseBody> {
        val filename = "members_${LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE)}.xlsx"
        val body = StreamingResponseBody { outputStream ->
            memberExcelService.writeMembersToExcel(outputStream)
        }

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(XLSX_CONTENT_TYPE))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$filename\"")
            .body(body)
    }

    companion object {
        private const val XLSX_CONTENT_TYPE =
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    }
}
