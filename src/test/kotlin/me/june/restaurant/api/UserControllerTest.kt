package me.june.restaurant.api

import com.fasterxml.jackson.databind.ObjectMapper
import me.june.restaurant.dto.UserDto
import me.june.restaurant.entity.User
import me.june.restaurant.repository.UserRepository
import me.june.restaurant.support.WithMockAdmin
import me.june.restaurant.vo.Gender
import me.june.restaurant.vo.Password
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest(
	private val userRepository: UserRepository,
	private val mockMvc: MockMvc,
	private val objectMapper: ObjectMapper,
) {

	companion object {
		const val BASE_URL = "/users"
	}

	@Test
	@WithMockAdmin
	@DisplayName("유저 목록 조회")
	fun `유저 목록 조회 api`() {
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
			get(BASE_URL)
				.param("page", "0")
				.param("size", "10")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE)
		).andDo(print())

		// then
		result.andExpect(status().isOk)
			.andExpect(jsonPath("$.page").exists())
			.andExpect(jsonPath("$.page.number").exists())
			.andExpect(jsonPath("$.page.number").value("0"))
			.andExpect(jsonPath("$._embedded.responseList").exists())
			.andExpect(jsonPath("$._embedded.responseList[0].id").exists())
			.andExpect(jsonPath("$._embedded.responseList[0].email").exists())
			.andExpect(jsonPath("$._embedded.responseList[0].birth").exists())
			.andExpect(jsonPath("$._embedded.responseList[0].gender").exists())
			.andExpect(jsonPath("$._embedded.responseList[0].createdAt").exists())
	}

	@Test
	@WithMockAdmin
	@DisplayName("유저 상세 조회")
	fun `유저 단건 조회 api`() {
		// given
		val user = User(
			password = Password("asdf"),
			username = "ncucu",
			name = "엔꾸꾸",
			email = "ncucu.me@kakaocommerce.com",
			birth = LocalDate.of(1994, 4, 13),
			gender = Gender.MAN
		)
		val savedUser = userRepository.save(user)

		// when
		val result = this.mockMvc.perform(
			get("${BASE_URL}/${savedUser.id}")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE)
		).andDo(print())

		// then
		result.andExpect(status().isOk)
			.andExpect(jsonPath("$.id").exists())
			.andExpect(jsonPath("$.username").exists())
			.andExpect(jsonPath("$.username").value("ncucu"))
			.andExpect(jsonPath("$.email").exists())
			.andExpect(jsonPath("$.email").value("ncucu.me@kakaocommerce.com"))
			.andExpect(jsonPath("$.birth").exists())
			.andExpect(jsonPath("$.gender").exists())
			.andExpect(jsonPath("$.createdAt").exists())
	}

	@Test
	@DisplayName("유저 생성")
	fun `유저 생성 api`() {
		// given
		val request = UserDto.CreateRequest(
			password = "asdf",
			username = "ncucu",
			name = "엔꾸꾸",
			email = "ncucu.me@kakaocommerce.com",
			birth = LocalDate.of(1994, 4, 13),
			gender = Gender.MAN
		)

		// when
		val result = this.mockMvc.perform(
			post(BASE_URL)
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE)
		).andDo(print())

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

	@Test
	@WithMockAdmin
	@DisplayName("유저 수정")
	fun `유저 수정 api`() {
		// given
		val user = User(
			password = Password("asdf"),
			username = "ncucu",
			name = "엔꾸꾸",
			email = "ncucu.me@kakaocommerce.com",
			birth = LocalDate.of(1994, 4, 13),
			gender = Gender.MAN
		)
		val savedUser = userRepository.save(user)

		val request = UserDto.UpdateRequest(
			password = "asdf1234",
			name = "엔꾸꾸",
			email = "ncucudas.me@kakaocommerce.com",
			birth = LocalDate.of(1994, 4, 13),
			gender = Gender.MAN,
		)

		// when
		val result = this.mockMvc.perform(
			put("${BASE_URL}/${savedUser.id}")
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE)
		).andDo(print())

		// then
		val (password, name, email, birth, gender) = request

		result.andExpect(status().isOk)
			.andExpect(jsonPath("$.id").exists())
			.andExpect(jsonPath("$.id").value(savedUser.id))
			.andExpect(jsonPath("$.name").exists())
			.andExpect(jsonPath("$.name").value(name))
			.andExpect(jsonPath("$.email").exists())
			.andExpect(jsonPath("$.email").value(email))
			.andExpect(jsonPath("$.birth").exists())
			.andExpect(jsonPath("$.birth").value(birth.toString()))
			.andExpect(jsonPath("$.gender").exists())
			.andExpect(jsonPath("$.gender").value(gender.name))
			.andExpect(jsonPath("$.createdAt").exists())
	}

	@Test
	@WithMockAdmin
	@DisplayName("유저 삭제")
	fun `유저 삭제 api`() {
		// given
		val user = User(
			password = Password("asdf"),
			username = "ncucu",
			name = "엔꾸꾸",
			email = "ncucu.me@kakaocommerce.com",
			birth = LocalDate.of(1994, 4, 13),
			gender = Gender.MAN
		)
		val savedUser = userRepository.save(user)

		// when
		val result = this.mockMvc.perform(
			delete("${BASE_URL}/${savedUser.id}")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE)
		).andDo(print())

		// then
		result.andExpect(status().isOk)
	}
}