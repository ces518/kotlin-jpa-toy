package me.june.restaurant.service

import me.june.restaurant.dto.FamousRestaurantDto
import me.june.restaurant.mapper.FamousRestaurantDtoMapper
import me.june.restaurant.repository.FamousRestaurantRepository
import me.june.restaurant.validator.FamousRestaurantValidator
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class FamousRestaurantService(
	private val repository: FamousRestaurantRepository,
	private val validator: FamousRestaurantValidator,
	private val mapper: FamousRestaurantDtoMapper,
) {

	@Transactional
	fun create(request: FamousRestaurantDto.CreateRequest): Long {
		val entity = mapper.dtoToEntity(request)
			.also(validator::validate)
		return repository.save(entity).id
	}

	@Transactional
	fun update(id: Long, request: FamousRestaurantDto.UpdateRequest) {
		val findEntity = find(id)
		val entity = mapper.dtoToEntity(request)
		findEntity.apply {
			this.title = entity.title
			this.subTitle = entity.subTitle
			this.description = entity.description
			this.foodCategoryId = entity.foodCategoryId
			this.regionCategoryId = entity.regionCategoryId
		}.also(validator::validate)
	}

	@Transactional
	fun delete(id: Long) {
		val findEntity = find(id)
		repository.delete(findEntity)
	}

	private fun find(id: Long) = repository.findByIdOrNull(id) ?: throw FamousRestaurantNotFoundException()
}

class FamousRestaurantNotFoundException : RuntimeException()