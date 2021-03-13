package me.june.restaurant.validator

import me.june.restaurant.entity.FamousRestaurant
import me.june.restaurant.entity.FoodCategory
import me.june.restaurant.entity.RegionCategory
import me.june.restaurant.exception.ValidationException
import me.june.restaurant.mapper.FamousRestaurantDtoMapper
import me.june.restaurant.repository.FoodCategoryRepository
import me.june.restaurant.repository.RegionCategoryRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import org.mockito.BDDMockito.*
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.TestConstructor
import java.util.*

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(classes = [FamousRestaurantValidator::class, FamousRestaurantDtoMapper::class])
internal class FamousRestaurantValidatorTest(
	private val validator: FamousRestaurantValidator,
) {

	@MockBean
	lateinit var foodCategoryRepository: FoodCategoryRepository

	@MockBean
	lateinit var regionCategoryRepository: RegionCategoryRepository

	val entity = FamousRestaurant(
		"정가네",
		"정가네",
		"정가네 입니다",
		1L,
		10L,
	)

	val mockFoodCategory = mock(FoodCategory::class.java)
	val mockRegionCategory = mock(RegionCategory::class.java)

	@Test
	fun `음식 카테고리 와 지역 카테고리 모두 존재한다면 벨리데이션에 성공한다`() {
		// given
		given(foodCategoryRepository.findById(anyLong())).willReturn(Optional.of(mockFoodCategory))
		given(regionCategoryRepository.findById(anyLong())).willReturn(Optional.of(mockRegionCategory))

		// when
		validator.validate(entity)

		// then
		verify(foodCategoryRepository).findById(anyLong())
		verify(regionCategoryRepository).findById(anyLong())
	}

	@Test
	fun `음식 카테고리가 존재하지 않는다면 벨리데이션에 실패한다`() {
		// given
		given(foodCategoryRepository.findById(anyLong())).willReturn(Optional.empty())
		given(regionCategoryRepository.findById(anyLong())).willReturn(Optional.of(mockRegionCategory))

		// when
		val ex = assertThrows<ValidationException> { validator.validate(entity) }

		// then
		verify(foodCategoryRepository).findById(anyLong())
		verify(regionCategoryRepository).findById(anyLong())

		assertThat(ex.errors.errorCount).isEqualTo(1)
		assertThat(ex.errors.allErrors.first().code).isEqualTo("foodCategoryId.notFound")
		assertThat(ex.errors.allErrors.first().defaultMessage).isEqualTo("${entity.foodCategoryId} 에 해당하는 카테고리는 존재하지 않습니다.")
	}

	@Test
	fun `지역 카테고리가 존재하지 않는다면 벨리데이션에 실패한다`() {
		// given
		given(foodCategoryRepository.findById(anyLong())).willReturn(Optional.of(mockFoodCategory))
		given(regionCategoryRepository.findById(anyLong())).willReturn(Optional.empty())

		// when
		val ex = assertThrows<ValidationException> { validator.validate(entity) }

		// then
		assertThat(ex.errors.errorCount).isEqualTo(1)
		assertThat(ex.errors.allErrors.first().code).isEqualTo("regionCategoryId.notFound")
		assertThat(ex.errors.allErrors.first().defaultMessage).isEqualTo("${entity.regionCategoryId} 에 해당하는 카테고리는 존재하지 않습니다.")
	}
}