package me.june.restaurant.api

import com.fasterxml.jackson.databind.ObjectMapper
import me.june.restaurant.dto.FoodCategoryDto
import me.june.restaurant.entity.FoodCategory
import me.june.restaurant.repository.FoodCategoryRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
@AutoConfigureMockMvc
class FoodCategoryControllerTest(
	private val foodCategoryRepository: FoodCategoryRepository,
	private val mockMvc: MockMvc,
	private val objectMapper: ObjectMapper,
) {

	companion object {
		const val BASE_URL = "/categories/food"
	}

	@Test
	fun `음식 카테고리 목록 조회에 성공한다`() {
		// given
		val foodCategories = List(10) { index -> FoodCategory(name = "카테고리명$index") }
		foodCategories.first().addChildren(foodCategories.last())
		foodCategoryRepository.saveAll(foodCategories)

		// when
		val result = mockMvc.perform(
			get(BASE_URL)
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
			.andExpect(jsonPath("$._embedded.responseList[0].name").exists())
			.andExpect(jsonPath("$._embedded.responseList[0].parentId").isEmpty)
			.andExpect(jsonPath("$._embedded.responseList[0].children").exists())
	}

	@Test
	fun `존재하는 음식 카테고리를 조회하면 성공한다`() {
		// given
		val savedCategory = foodCategoryRepository.save(FoodCategory(name = "한식"))

		// when
		val result = mockMvc.perform(
			get("${BASE_URL}/${savedCategory.id}")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE)
		).andDo(print())

		// then
		result.andExpect(status().isOk)
			.andExpect(jsonPath("$.id").value(savedCategory.id))
			.andExpect(jsonPath("$.name").value(savedCategory.name))
			.andExpect(jsonPath("$.parentId").isEmpty)
			.andExpect(jsonPath("$._links.profile").exists())
	}

	@Test
	fun `존재하지 않는 음식 카테고리를 조회하면 실패한다`() {
		// given

		// when
		val result = mockMvc.perform(
			get("${BASE_URL}/1")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE)
		).andDo(print())

		// then
		result.andExpect(status().isNotFound)
	}


	@Test
	fun `음식 카테고리 등록에 성공한다`() {
		// given
		val request = FoodCategoryDto.CreateRequest(name = "한식", parentId = null)

		// when
		val result = mockMvc.perform(
			post(BASE_URL)
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE)
		).andDo(print())

		// then
		result.andExpect(status().isCreated)
			.andExpect(jsonPath("$.id").exists())
			.andExpect(jsonPath("$.name").value(request.name))
			.andExpect(jsonPath("$.parentId").isEmpty)
			.andExpect(jsonPath("$.children").isEmpty)
			.andExpect(jsonPath("$._links.profile").exists())
	}

	@Test
	fun `존재하는 음식 카테고리를 수정하면 성공한다`() {
		// given
		val savedCategory = foodCategoryRepository.save(FoodCategory(name = "한식"))

		val request = FoodCategoryDto.UpdateRequest(name = "중식", parentId = null)

		// when
		val result = mockMvc.perform(
			put("${BASE_URL}/${savedCategory.id}")
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE)
		).andDo(print())

		// then
		result.andExpect(status().isOk)
			.andExpect(jsonPath("$.id").value(savedCategory.id))
			.andExpect(jsonPath("$.name").value(request.name))
			.andExpect(jsonPath("$.parentId").isEmpty)
			.andExpect(jsonPath("$.children").isEmpty)
			.andExpect(jsonPath("$._links.profile").exists())
	}

	@Test
	fun `존재하지 않는 음식 카테고리를 수정하면 실패한다`() {
		// given
		val request = FoodCategoryDto.UpdateRequest(name = "중식", parentId = null)

		// when
		val result = mockMvc.perform(
			put("${BASE_URL}/1")
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE)
		).andDo(print())

		// then
		result.andExpect(status().isNotFound)
	}

	@Test
	fun `존재하는 음식 카테고리를 삭제하면 성공한다`() {
		// given
		val savedCategory = foodCategoryRepository.save(FoodCategory(name = "한식"))

		// when
		val result = mockMvc.perform(
			delete("${BASE_URL}/${savedCategory.id}")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE)
		).andDo(print())

		// then
		result.andExpect(status().isOk)
			.andExpect(jsonPath("$.value").value(true))
	}

	@Test
	fun `존재하지 않는 음식 카테고리를 삭제하면 실패한다`() {
		// given

		// when
		val result = mockMvc.perform(
			delete("${BASE_URL}/1")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE)
		).andDo(print())

		// then
		result.andExpect(status().isNotFound)
	}
}