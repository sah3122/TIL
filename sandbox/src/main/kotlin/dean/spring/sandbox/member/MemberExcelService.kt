package dean.spring.sandbox.member

import dean.spring.sandbox.log
import org.dhatim.fastexcel.Workbook
import org.dhatim.fastexcel.Worksheet
import org.springframework.stereotype.Service
import java.io.OutputStream

@Service
class MemberExcelService(private val memberQueryService: MemberQueryService) {

    fun writeMembersToExcel(outputStream: OutputStream) {
        Workbook(outputStream, "SandboxApp", "1.0").use { workbook ->
            val ws = workbook.newWorksheet("Members")
            writeHeader(ws)
            memberQueryService.withMemberStream { stream ->
                var row = 1
                log.info("member stream 조회 $row 회")
                Thread.sleep(1000)
                stream.forEach { member ->
                    writeRow(ws, row++, member)
                }
            }
        }
    }

    private fun writeHeader(ws: Worksheet) {
        val headers = listOf("ID", "이름", "이메일", "전화번호", "상태", "생년월일", "가입일")
        headers.forEachIndexed { col, header ->
            ws.value(0, col, header)
        }
    }

    private fun writeRow(ws: Worksheet, rowIdx: Int, member: Member) {
        ws.value(rowIdx, 0, member.id.toDouble())
        ws.value(rowIdx, 1, member.name)
        ws.value(rowIdx, 2, member.email)
        ws.value(rowIdx, 3, member.phoneNumber)
        ws.value(rowIdx, 4, member.status)
        ws.value(rowIdx, 5, member.birthDate.toString())
        ws.value(rowIdx, 6, member.createdAt.toString())
    }

}
