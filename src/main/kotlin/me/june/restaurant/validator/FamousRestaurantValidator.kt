package me.june.restaurant.validator

import me.june.restaurant.entity.FamousRestaurant
import me.june.restaurant.exception.ValidationException
import me.june.restaurant.repository.FoodCategoryRepository
import me.june.restaurant.repository.RegionCategoryRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.validation.BeanPropertyBindingResult

@Component
class FamousRestaurantValidator(
	private val foodCategoryRepository: FoodCategoryRepository,
	private val regionCategoryRepository: RegionCategoryRepository,
) {

	fun validate(entity: FamousRestaurant) {
		val errors = BeanPropertyBindingResult(entity, FamousRestaurant::class.java.simpleName)

		if (foodCategoryRepository.findByIdOrNull(entity.foodCategoryId) == null) {
			errors.rejectValue(
				"foodCategoryId",
				"foodCategoryId.notFound",
				arrayOf("음식 카테고리"),
				"${entity.foodCategoryId} 에 해당하는 카테고리는 존재하지 않습니다."
			)
		}

		if (regionCategoryRepository.findByIdOrNull(entity.regionCategoryId) == null) {
			errors.rejectValue(
				"regionCategoryId",
				"regionCategoryId.notFound",
				arrayOf("지역 카테고리"),
				"${entity.regionCategoryId} 에 해당하는 카테고리는 존재하지 않습니다."
			)
		}

		if (errors.hasErrors()) {
			throw ValidationException(errors)
		}
	}
}