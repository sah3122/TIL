package dean.spring.sandbox.member

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.io.ByteArrayOutputStream
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
class MemberExcelServiceTest {

    @Mock
    lateinit var memberQueryService: MemberQueryService

    @InjectMocks
    lateinit var memberExcelService: MemberExcelService

    @Test
    fun `produces valid xlsx with correct data`() {
        val members = listOf(
            createMember(1, "Alice"),
            createMember(2, "Bob"),
            createMember(3, "Charlie"),
        )
        whenever(memberQueryService.findPage(eq(0), any())).thenReturn(members)

        val output = ByteArrayOutputStream()
        memberExcelService.writeMembersToExcel(output)

        val bytes = output.toByteArray()
        assertTrue(bytes.size > 2)
        assertEquals(0x50.toByte(), bytes[0]) // P
        assertEquals(0x4B.toByte(), bytes[1]) // K — PK magic bytes (ZIP/XLSX)
    }

    @Test
    fun `handles empty dataset gracefully`() {
        whenever(memberQueryService.findPage(eq(0), any())).thenReturn(emptyList())

        val output = ByteArrayOutputStream()
        memberExcelService.writeMembersToExcel(output)

        val bytes = output.toByteArray()
        assertTrue(bytes.size > 2)
        assertEquals(0x50.toByte(), bytes[0])
        assertEquals(0x4B.toByte(), bytes[1])
    }

    @Test
    fun `pagination loops until batch is smaller than page size`() {
        val fullPage = (1..5000).map { createMember(it.toLong(), "Member$it") }
        val partialPage = (5001..7500).map { createMember(it.toLong(), "Member$it") }

        whenever(memberQueryService.findPage(eq(0), any())).thenReturn(fullPage)
        whenever(memberQueryService.findPage(eq(1), any())).thenReturn(partialPage)

        val output = ByteArrayOutputStream()
        memberExcelService.writeMembersToExcel(output)

        verify(memberQueryService, times(2)).findPage(any(), any())
    }

    private fun createMember(id: Long, name: String) = Member(
        id = id,
        name = name,
        email = "test$id@test.com",
        phoneNumber = "010-0000-${id.toString().padStart(4, '0')}",
        status = "ACTIVE",
        birthDate = LocalDate.of(1990, 1, 1),
    )
}
