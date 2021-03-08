package me.june.restaurant.service

import me.june.restaurant.dto.AbstractCategoryDto
import me.june.restaurant.entity.Category
import me.june.restaurant.exception.CategoryNotFoundException
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
abstract class GenericCategoryService<T : Category>(
	private val repository: JpaRepository<T, Long>
) {

	fun findCategory(id: Long) = repository.findByIdOrNull(id) ?: throw CategoryNotFoundException()

	fun findCategories(ids: List<Long>) = repository.findAllById(ids)

	@Transactional
	fun createCategory(dto: AbstractCategoryDto): Long {
		val parentCategory = dto.parentId?.let(this::findCategory)
		val entity = dto.toEntity(parentCategory)
		return repository.save(entity).id
	}

	@Transactional
	fun updateCategory(id: Long, dto: AbstractCategoryDto) {
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

	abstract fun AbstractCategoryDto.toEntity(entity: T?): T
}