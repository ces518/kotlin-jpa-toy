package me.june.restaurant.service

import me.june.restaurant.dto.RegionCategoryDto
import me.june.restaurant.entity.RegionCategory
import me.june.restaurant.exception.CategoryNotFoundException
import me.june.restaurant.repository.RegionCategoryRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class RegionCategoryService(
	private val repository: RegionCategoryRepository,
) {

	fun findCategory(id: Long) = repository.findByIdOrNull(id) ?: throw CategoryNotFoundException()

	fun findCategories(ids: List<Long>) = repository.findAllById(ids)

	@Transactional
	fun createCategory(dto: RegionCategoryDto.CreateRequest): Long {
		val parentCategory = dto.parentId?.let(this::findCategory)
		val entity = dto.toEntity(parentCategory)
		return repository.save(entity).id
	}

	@Transactional
	fun updateCategory(id: Long, dto: RegionCategoryDto.UpdateRequest) {
		val findEntity = findCategory(id)
		val parentCategory = dto.parentId?.let(this::findCategory)
		val entity = dto.toEntity(parentCategory)
		findEntity.update(entity)
	}

	@Transactional
	fun deleteCategory(id: Long) {
		val findEntity = findCategory(id)
		repository.delete(findEntity)
	}
}

fun RegionCategoryDto.CreateRequest.toEntity(parent: RegionCategory?): RegionCategory {
	return RegionCategory(
		name = this.name,
		parent = parent,
	)
}

fun RegionCategoryDto.UpdateRequest.toEntity(parent: RegionCategory?): RegionCategory {
	return RegionCategory(
		name = this.name,
		parent = parent,
	)
}