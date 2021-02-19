package me.june.restaurant.service

import me.june.restaurant.dto.FoodCategoryDto
import me.june.restaurant.entity.FoodCategory
import me.june.restaurant.repository.FoodCategoryRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.*
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.TestConstructor
import java.util.*

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(classes = [FoodCategoryService::class])
@EnableAutoConfiguration
internal class FoodCategoryServiceTest(
    private val service: FoodCategoryService,
) {

    @MockBean
    private lateinit var repository: FoodCategoryRepository

    @Test
    @DisplayName("상위 음식 카테고리 생성 성공")
    fun `상위 음식 카테고리 생성에 성공한다`() {
        // given
        val request = FoodCategoryDto.CreateRequest(name = "한식", parentId = null)

        val mockCategory = FoodCategory(name = request.name)

        given(repository.save(any(FoodCategory::class.java))).willReturn(mockCategory)
        given(repository.findById(anyLong())).willReturn(Optional.of(mockCategory))

        // when
        val savedCategoryId = service.createCategory(request)

        // then
        val ( name, parentId ) = request
        val findCategory = repository.findByIdOrNull(savedCategoryId)

        assertThat(findCategory).isNotNull
        findCategory!!
        assertThat(findCategory.name).isEqualTo(name)
        assertThat(findCategory.parent).isNull()
    }
}