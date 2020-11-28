package me.june.restaurant.api

import me.june.restaurant.entity.User
import me.june.restaurant.repository.UserRepository
import me.june.restaurant.vo.Gender
import me.june.restaurant.vo.Password
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
@AutoConfigureMockMvc
class SignUpControllerTest(
        private val userRepository: UserRepository,
        private val mockMvc: MockMvc,
) {

    @Test
    fun `이미 가입된 사용자명은 중복체크시 실패한다`() {
        // given
        val user = User(password = Password("asdf"),
                username = "ncucu",
                name = "엔꾸꾸",
                email = "ncucu.me@kakaocommerce.com",
                birth = LocalDate.of(1994, 4, 13),
                gender = Gender.MAN)
        userRepository.save(user)

        // when
        val result = mockMvc.perform(put("/signup/check")
                .param("username", user.username)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andDo(print())

        // then
        result.andExpect(status().isOk)
        result.andExpect(jsonPath("$.joineable").exists())
        result.andExpect(jsonPath("$.joineable").value(false))
    }
}