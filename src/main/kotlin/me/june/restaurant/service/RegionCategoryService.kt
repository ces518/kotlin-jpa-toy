package me.june.restaurant.service

import me.june.restaurant.dto.AbstractCategoryDto
import me.june.restaurant.entity.RegionCategory
import me.june.restaurant.repository.RegionCategoryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class RegionCategoryService(
	private val repository: RegionCategoryRepository,
) : GenericCategoryService<RegionCategory>(repository) {

	override fun AbstractCategoryDto.toEntity(entity: RegionCategory?): RegionCategory {
		return RegionCategory(
			name = this.name,
			parent = entity,
		)
	}
}