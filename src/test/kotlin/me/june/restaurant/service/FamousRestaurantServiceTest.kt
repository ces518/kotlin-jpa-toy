package me.june.restaurant.service

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import me.june.restaurant.dto.FamousRestaurantDto
import me.june.restaurant.entity.FamousRestaurant
import me.june.restaurant.exception.ValidationException
import me.june.restaurant.mapper.FamousRestaurantDtoMapper
import me.june.restaurant.repository.FamousRestaurantRepository
import me.june.restaurant.validator.FamousRestaurantValidator
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.BDDMockito.given

import org.mockito.Mockito.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.TestConstructor
import java.util.*

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(classes = [FamousRestaurantService::class])
internal class FamousRestaurantServiceTest(
	private val service: FamousRestaurantService,
) {

	@MockBean
	lateinit var validator: FamousRestaurantValidator

	@MockBean
	lateinit var repository: FamousRestaurantRepository

	@MockBean
	lateinit var mapper: FamousRestaurantDtoMapper

	val FOOD_CATEGORY_ID = 1L
	val REGION_CATEGORY_ID = 10L
	val MOCK_RESTAURANT = mock(FamousRestaurant::class.java)
	val MOCK_RESTAURANT_ID = 1L

	@Test
	fun `벨리데이션에 성공하면 맛집 등록에 성공한다`() {
		// given
		val request = FamousRestaurantDto.CreateRequest(
			title = "정가네",
			subTitle = "정가네",
			description = "정가네 입니다",
			foodCategoryId = FOOD_CATEGORY_ID,
			regionCategoryId = REGION_CATEGORY_ID,
		)

		given(mapper.dtoToEntity(any<FamousRestaurantDto.CreateRequest>())).willReturn(MOCK_RESTAURANT)
		doNothing().whenever(validator).validate(any())
		given(repository.save(any<FamousRestaurant>())).willReturn(MOCK_RESTAURANT)
		given(MOCK_RESTAURANT.id).willReturn(MOCK_RESTAURANT_ID)

		// when
		service.create(request)

		// then
		verify(mapper).dtoToEntity(any<FamousRestaurantDto.CreateRequest>())
		verify(validator).validate(any())
	}

	@Test
	fun `벨리데이션에 실패하면 맛집 등록에 실패한다`() {
		// given
		val request = FamousRestaurantDto.CreateRequest(
			title = "정가네",
			subTitle = "정가네",
			description = "정가네 입니다",
			foodCategoryId = FOOD_CATEGORY_ID,
			regionCategoryId = REGION_CATEGORY_ID,
		)

		given(mapper.dtoToEntity(any<FamousRestaurantDto.CreateRequest>())).willReturn(MOCK_RESTAURANT)
		doThrow(ValidationException::class.java).whenever(validator).validate(any())
		given(repository.save(any<FamousRestaurant>())).willReturn(MOCK_RESTAURANT)
		given(MOCK_RESTAURANT.id).willReturn(MOCK_RESTAURANT_ID)

		// when
		assertThrows<ValidationException> { service.create(request) }

		// then
		verify(mapper).dtoToEntity(any<FamousRestaurantDto.CreateRequest>())
		verify(validator).validate(any())
	}

	@Test
	fun `존재하는 맛집이고 벨리데이션에 성공하면 맛집 수정에 성공한다`() {
		// given
		val request = FamousRestaurantDto.UpdateRequest(
			title = "정가네 수정",
			subTitle = "정가네 수정",
			description = "정가네 수정 입니다",
			foodCategoryId = FOOD_CATEGORY_ID,
			regionCategoryId = REGION_CATEGORY_ID,
		)

		given(repository.findById(anyLong())).willReturn(Optional.of(MOCK_RESTAURANT))
		given(mapper.dtoToEntity(any<FamousRestaurantDto.UpdateRequest>())).willReturn(MOCK_RESTAURANT)
		doNothing().whenever(validator).validate(any())

		// when
		service.update(1L, request)

		// then
		verify(repository).findById(anyLong())
		verify(mapper).dtoToEntity(any<FamousRestaurantDto.UpdateRequest>())
		verify(validator).validate(any())
	}

	@Test
	fun `존재하지 않는 맛집이면 맛집 수정에 실패한다`(){
		// given
		val request = FamousRestaurantDto.UpdateRequest(
			title = "정가네 수정",
			subTitle = "정가네 수정",
			description = "정가네 수정 입니다",
			foodCategoryId = FOOD_CATEGORY_ID,
			regionCategoryId = REGION_CATEGORY_ID,
		)
		given(repository.findById(anyLong())).willReturn(Optional.empty())

		// when
		assertThrows<FamousRestaurantNotFoundException> { service.update(1L, request) }

		// then
		verify(repository).findById(anyLong())
	}

	@Test
	fun `존재하는 맛집이고 벨리데이션에 실패하면 맛집 수정에 실패한다`() {
		// given
		val request = FamousRestaurantDto.UpdateRequest(
			title = "정가네 수정",
			subTitle = "정가네 수정",
			description = "정가네 수정 입니다",
			foodCategoryId = FOOD_CATEGORY_ID,
			regionCategoryId = REGION_CATEGORY_ID,
		)

		given(repository.findById(anyLong())).willReturn(Optional.of(MOCK_RESTAURANT))
		given(mapper.dtoToEntity(any<FamousRestaurantDto.UpdateRequest>())).willReturn(MOCK_RESTAURANT)
		doThrow(ValidationException::class.java).whenever(validator).validate(any())

		// when
		assertThrows<ValidationException> { service.update(1L, request) }

		// then
		verify(repository).findById(anyLong())
		verify(mapper).dtoToEntity(any<FamousRestaurantDto.UpdateRequest>())
		verify(validator).validate(any())
	}


	@Test
	fun `존재하는 맛집이면 맛집 삭제에 성공한다`() {
		// given
		given(repository.findById(anyLong())).willReturn(Optional.of(MOCK_RESTAURANT))

		// when
		service.delete(1L)

		// then
		verify(repository).findById(anyLong())
		verify(repository).delete(MOCK_RESTAURANT)
	}
}