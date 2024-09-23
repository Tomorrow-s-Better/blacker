package com.blacker

import com.blacker.security.JwtUtil
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
class HelloControllerTest(@Autowired val mockMvc: MockMvc) {

    private var jwtToken: String? = null

    @BeforeEach
    fun setUp() {
        // 최초에만 인증 요청을 통해 JWT 토큰 발급
        if (jwtToken == null) {
            val result = mockMvc.perform(
                MockMvcRequestBuilders.post("/authenticate")
                    .content("""{"username": "testUser", "password": "testPassword"}""")
                    .contentType(MediaType.APPLICATION_JSON)
            )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()

            // 응답에서 JWT 토큰 추출
            jwtToken = result.response.contentAsString // 여기에서 토큰을 적절히 파싱
        }
    }

    @Test
    fun `should return hello message when authenticated`() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/hello")
                .header(HttpHeaders.AUTHORIZATION, "Bearer $jwtToken")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().string("Hello Spring"))
    }

    @Test
    fun `should return unauthorized when not authenticated`() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/hello")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isForbidden)
    }
}
