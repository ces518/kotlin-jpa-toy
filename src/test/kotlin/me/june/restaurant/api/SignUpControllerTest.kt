package me.june.restaurant.api

import com.fasterxml.jackson.databind.ObjectMapper
import me.june.restaurant.dto.SignUpDto
import me.june.restaurant.entity.User
import me.june.restaurant.repository.UserRepository
import me.june.restaurant.service.DuplicateUsernameException
import me.june.restaurant.vo.Gender
import me.june.restaurant.vo.Password
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SignUpControllerTest(
	private val userRepository: UserRepository,
	private val mockMvc: MockMvc,
	private val objectMapper: ObjectMapper,
) {

	companion object {
		const val BASE_URL = "/signup"
	}

	@Test
	fun `이미 가입된 사용자명은 중복체크시 실패한다`() {
		// given
		val user = User(
			password = Password("asdf"),
			username = "ncucu",
			name = "엔꾸꾸",
			email = "ncucu.me@kakaocommerce.com",
			birth = LocalDate.of(1994, 4, 13),
			gender = Gender.MAN
		)
		userRepository.save(user)

		// when
		val result = mockMvc.perform(
			put("${BASE_URL}/check")
				.param("username", user.username)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE)
		).andDo(print())

		// then
		result.andExpect(status().isOk)
		result.andExpect(jsonPath("$.joineable").exists())
		result.andExpect(jsonPath("$.joineable").value(false))
	}

	@Test
	fun `존재하는 아이디는 회원가입에 실패한다`() {
		// given
		val user = User(
			password = Password("asdf"),
			username = "ncucu",
			name = "엔꾸꾸",
			email = "ncucu.me@kakaocommerce.com",
			birth = LocalDate.of(1994, 4, 13),
			gender = Gender.MAN
		)
		userRepository.save(user)

		val request = SignUpDto.Request(
			password = "asdf",
			username = "ncucu",
			name = "엔꾸꾸",
			email = "ncucu.me@kakaocommerce.com",
			birth = LocalDate.of(1994, 4, 13),
			gender = Gender.MAN
		)

		// when
		val result = mockMvc.perform(
			post(BASE_URL)
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE)
		).andDo(print())

		// then
		result.andExpect(status().isConflict)
	}

	@Test
	fun `회원가입에 성공한다`() {
		// given
		val request = SignUpDto.Request(
			password = "asdf",
			username = "ncucu",
			name = "엔꾸꾸",
			email = "ncucu.me@kakaocommerce.com",
			birth = LocalDate.of(1994, 4, 13),
			gender = Gender.MAN
		)

		// when
		val result = mockMvc.perform(
			post(BASE_URL)
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE)
		)
			.andDo(print())

		// then
		val (password, username, name, email, birth, gender) = request

		result.andExpect(status().isCreated)
			.andExpect(jsonPath("$.id").exists())
			.andExpect(jsonPath("$.username").exists())
			.andExpect(jsonPath("$.username").value(username))
			.andExpect(jsonPath("$.email").exists())
			.andExpect(jsonPath("$.email").value(email))
			.andExpect(jsonPath("$.birth").exists())
			.andExpect(jsonPath("$.birth").value(birth.toString()))
			.andExpect(jsonPath("$.gender").exists())
			.andExpect(jsonPath("$.gender").value(gender.name))
			.andExpect(jsonPath("$.createdAt").exists())
	}
}