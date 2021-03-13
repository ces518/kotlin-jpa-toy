package me.june.restaurant.service

import me.june.restaurant.dto.FamousRestaurantDto
import me.june.restaurant.mapper.FoodCategoryDtoMapper
import me.june.restaurant.mapper.RegionCategoryDtoMapper
import me.june.restaurant.repository.FamousRestaurantRepository
import me.june.restaurant.repository.query.FamousRestaurantQueryRepository
import me.june.restaurant.repository.query.FamousRestaurantSearch
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class FamousRestaurantQueryService(
	private val repository: FamousRestaurantRepository,
	private val queryRepository: FamousRestaurantQueryRepository,
	private val foodCategoryService: FoodCategoryService,
	private val regionCategoryService: RegionCategoryService,
	private val foodCategoryDtoMapper: FoodCategoryDtoMapper,
	private val regionCategoryDtoMapper: RegionCategoryDtoMapper,
) {

	// 목록 조회
	fun findAll(pageable: Pageable, search: FamousRestaurantSearch): Page<FamousRestaurantDto.Response> {
		return queryRepository.findAll(pageable, search)
	}

	// 조회
	fun find(id: Long): FamousRestaurantDto.Response {
		val entity = repository.findByIdOrNull(id) ?: throw FamousRestaurantNotFoundException()
		return FamousRestaurantDto.Response(
			id = entity.id,
			title = entity.title,
			subTitle = entity.subTitle,
			description = entity.description,
			foodCategory = foodCategoryService.findCategory(entity.foodCategoryId)
				.let(foodCategoryDtoMapper::entityToDto),
			regionCategory = regionCategoryService.findCategory(entity.regionCategoryId)
				.let(regionCategoryDtoMapper::entityToDto)
		)
	}
}
