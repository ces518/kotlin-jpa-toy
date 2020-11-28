package me.june.restaurant.api

import com.fasterxml.jackson.databind.ObjectMapper
import me.june.restaurant.dto.LoginDto
import me.june.restaurant.entity.User
import me.june.restaurant.repository.UserRepository
import me.june.restaurant.vo.Gender
import me.june.restaurant.vo.Password
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
@AutoConfigureMockMvc
internal class LoginControllerTest(
        private val userRepository: UserRepository,
        private val mockMvc: MockMvc,
        private val objectMapper: ObjectMapper,
) {

    @Test
    fun `로그인에 성공한다`() {
        // given
        val user = User(password = Password("asdf"),
                username = "ncucudas",
                name = "엔꾸꾸",
                email = "ncucu.me@kakaocommerce.com",
                birth = LocalDate.of(1994, 4, 13),
                gender = Gender.MAN)
        userRepository.save(user)

        val request = LoginDto.Request(username = "ncucudas", password = "asdf")

        // when
        val result = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())

        // then
        result.andExpect(status().isOk)
                .andExpect(jsonPath("$.token").exists())
    }
}