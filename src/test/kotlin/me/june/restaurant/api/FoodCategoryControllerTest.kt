package me.june.restaurant.api

import com.fasterxml.jackson.databind.ObjectMapper
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

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
@AutoConfigureMockMvc
class FoodCategoryControllerTest(
        private val foodCategoryRepository: FoodCategoryRepository,
        private val mockMvc: MockMvc,
        private val objectMapper: ObjectMapper,
) {

    @Test
    fun `음식 카테고리 조회에 성공한다`() {
        // given
        val foodCategories = List(10) { index ->  FoodCategory(name = "카테고리명$index") }
        foodCategoryRepository.saveAll(foodCategories)

        // when
        val result = mockMvc.perform(get("/categories/food")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())


        // then
    }

}