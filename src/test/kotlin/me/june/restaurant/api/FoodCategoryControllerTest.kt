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

    @Test
    fun `음식 카테고리 조회에 성공한다`() {
        // given
        val foodCategories = List(10) { index ->  FoodCategory(name = "카테고리명$index") }
        foodCategories.first().addChildren(foodCategories.last())
        foodCategoryRepository.saveAll(foodCategories)

        // when
        val result = mockMvc.perform(get("/categories/food")
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                )
                .andDo(print())

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

}