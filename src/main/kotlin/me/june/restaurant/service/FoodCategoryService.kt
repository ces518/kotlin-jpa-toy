package me.june.restaurant.service

import me.june.restaurant.dto.AbstractCategoryDto
import me.june.restaurant.entity.FoodCategory
import me.june.restaurant.repository.FoodCategoryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class FoodCategoryService(
	private val repository: FoodCategoryRepository,
) : GenericCategoryService<FoodCategory>(repository) {

	override fun AbstractCategoryDto.toEntity(entity: FoodCategory?): FoodCategory {
		return FoodCategory(
			name = this.name,
			parent = entity
		)
	}
}
