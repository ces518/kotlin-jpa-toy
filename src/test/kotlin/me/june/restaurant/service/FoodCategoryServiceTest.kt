package me.june.restaurant.service

import me.june.restaurant.dto.FoodCategoryDto
import me.june.restaurant.entity.FoodCategory
import me.june.restaurant.exception.CategoryNotFoundException
import me.june.restaurant.repository.FoodCategoryRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
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
class FoodCategoryServiceTest(
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
		verify(repository).save(mockCategory)

		val (name, parentId) = request
		val findCategory = repository.findByIdOrNull(savedCategoryId)

		assertThat(findCategory).isNotNull
		findCategory!!
		assertThat(findCategory.name).isEqualTo(name)
		assertThat(findCategory.parent).isNull()
	}

	@Test
	@DisplayName("하위 음식 카테고리 생성 성공")
	fun `하위 음식 카테고리 생성에 성공한다`() {
		// given
		val request = FoodCategoryDto.CreateRequest(name = "김치찌개", parentId = 1L)

		val mockParentCategory = FoodCategory(name = "한식")
		val mockCategory = FoodCategory(name = request.name, parent = mockParentCategory)

		given(repository.save(any(FoodCategory::class.java))).willReturn(mockCategory)
		given(repository.findById(request.parentId!!)).willReturn(Optional.of(mockParentCategory))
		given(repository.findById(anyLong())).willReturn(Optional.of(mockCategory))

		// when
		val savedCategoryId = service.createCategory(request)

		// then
		verify(repository).findById(request.parentId!!)
		verify(repository).save(mockCategory)

		val (name, parentId) = request
		val findCategory = repository.findByIdOrNull(savedCategoryId)

		assertThat(findCategory).isNotNull
		findCategory!!
		assertThat(findCategory.parent).isNotNull

		val parentCategory = findCategory.parent!!
		assertThat(parentCategory.name).isEqualTo(parentCategory.name)
		assertThat(findCategory.name).isEqualTo(name)
		assertThat(findCategory.parent!!.id).isEqualTo(parentCategory.id)
	}

	@Test
	@DisplayName("상위 카테고리 수정 성공")
	fun `상위 카테고리 수정에 성공한다`() {
		// given
		val request = FoodCategoryDto.UpdateRequest(name = "중식", parentId = null)

		val mockCategory = FoodCategory(name = "한식")

		given(repository.findById(anyLong())).willReturn(Optional.of(mockCategory))

		// when
		service.updateCategory(1L, request)

		// then
		verify(repository).findById(1L)

		val findCategory = repository.findByIdOrNull(1L)
		assertThat(findCategory).isNotNull
		findCategory!!

		assertThat(findCategory.name).isEqualTo(request.name)
	}

	@Test
	@DisplayName("존재하지 않는 카테고리 수정 실패")
	fun `존재하지 않는 카테고리 수정에 실패한다`() {
		// given
		val request = FoodCategoryDto.UpdateRequest(name = "중식", parentId = null)

		given(repository.findById(anyLong())).willReturn(Optional.empty())

		// when
		val ex = assertThrows<CategoryNotFoundException> {
			service.updateCategory(1L, request)
		}

		// then
		verify(repository).findById(1L)
	}

	@Test
	@DisplayName("존재하지 않는 상위 카테고리를 부모로 지정시 수정 실패")
	fun `존재하지 않는 상위 카테고리를 부모로 지정시 수정에 실패한다`() {
		// given
		val request = FoodCategoryDto.UpdateRequest(name = "김치찌개", parentId = 10L)

		val mockParentCategory = FoodCategory(name = "한식")
		val mockCategory = FoodCategory(name = request.name, parent = mockParentCategory)

		given(repository.findById(request.parentId!!)).willReturn(Optional.empty())
		given(repository.findById(1L)).willReturn(Optional.of(mockCategory))

		// when
		val ex = assertThrows<CategoryNotFoundException> {
			service.updateCategory(1L, request)
		}

		// then
		verify(repository).findById(1L)
		verify(repository).findById(10L)
	}

	@Test
	@DisplayName("음식 카테고리 삭제 성공")
	fun `음식 카테고리 삭제에 성공한다`() {
		// given
		val requestId = 1L

		val mockCategory = FoodCategory(name = "한식")
		given(repository.findById(requestId)).willReturn(Optional.of(mockCategory))

		// when
		service.deleteCategory(requestId)

		// then
		verify(repository).delete(mockCategory)
	}

	@Test
	@DisplayName("음삭 카테고리 삭제 실패")
	fun `존재하지 않는 음식 카테고리 삭제시 실패한다`() {
		// given
		val requestId = 1L

		// when
		val ex = assertThrows<CategoryNotFoundException> {
			service.deleteCategory(requestId)
		}

		// then
	}
}