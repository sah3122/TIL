package dean.spring.sandbox.member

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.request
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MemberExcelControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var memberRepository: MemberRepository

    @BeforeEach
    fun setUp() {
        memberRepository.deleteAll()
        val members = (1..500).map { i ->
            Member(
                name = "Member$i",
                email = "test$i@test.com",
                phoneNumber = "010-0000-${i.toString().padStart(4, '0')}",
                status = "ACTIVE",
                birthDate = LocalDate.of(1990, 1, 1),
            )
        }
        memberRepository.saveAll(members)
    }

    @Test
    fun `GET excel returns 200 with correct headers`() {
        val asyncResult = mockMvc.perform(get("/api/members/excel"))
            .andExpect(request().asyncStarted())
            .andReturn()

        val result = mockMvc.perform(asyncDispatch(asyncResult))
            .andExpect(status().isOk)
            .andExpect(header().string("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
            .andExpect(header().exists("Content-Disposition"))
            .andReturn()

        val contentDisposition = result.response.getHeader("Content-Disposition")!!
        assertTrue(contentDisposition.contains("attachment"))
        assertTrue(contentDisposition.contains(".xlsx"))
    }

    @Test
    fun `response body is valid xlsx`() {
        val asyncResult = mockMvc.perform(get("/api/members/excel"))
            .andExpect(request().asyncStarted())
            .andReturn()

        val result = mockMvc.perform(asyncDispatch(asyncResult))
            .andExpect(status().isOk)
            .andReturn()

        val bytes = result.response.contentAsByteArray
        assertTrue(bytes.size > 2)
        assertEquals(0x50.toByte(), bytes[0])
        assertEquals(0x4B.toByte(), bytes[1])
    }
}
