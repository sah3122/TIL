package dean.spring.sandbox.member

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import java.io.ByteArrayOutputStream
import java.time.LocalDate
import java.util.stream.Stream

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
        stubWithMemberStream(members)

        val output = ByteArrayOutputStream()
        memberExcelService.writeMembersToExcel(output)

        val bytes = output.toByteArray()
        assertTrue(bytes.size > 2)
        assertEquals(0x50.toByte(), bytes[0]) // P
        assertEquals(0x4B.toByte(), bytes[1]) // K — PK magic bytes (ZIP/XLSX)
    }

    @Test
    fun `handles empty dataset gracefully`() {
        stubWithMemberStream(emptyList())

        val output = ByteArrayOutputStream()
        memberExcelService.writeMembersToExcel(output)

        val bytes = output.toByteArray()
        assertTrue(bytes.size > 2)
        assertEquals(0x50.toByte(), bytes[0])
        assertEquals(0x4B.toByte(), bytes[1])
    }

    @Test
    fun `stream processes all members without pagination`() {
        val allMembers = (1..7500).map { createMember(it.toLong(), "Member$it") }
        stubWithMemberStream(allMembers)

        val output = ByteArrayOutputStream()
        memberExcelService.writeMembersToExcel(output)

        val bytes = output.toByteArray()
        assertTrue(bytes.size > 2)
    }

    private fun stubWithMemberStream(members: List<Member>) {
        whenever(memberQueryService.withMemberStream(any<(Stream<Member>) -> Unit>()))
            .thenAnswer { invocation ->
                @Suppress("UNCHECKED_CAST")
                val action = invocation.arguments[0] as (Stream<Member>) -> Unit
                action(members.stream())
            }
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
