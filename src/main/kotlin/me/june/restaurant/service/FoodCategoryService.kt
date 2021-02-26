package me.june.restaurant.service

import me.june.restaurant.dto.FoodCategoryDto
import me.june.restaurant.entity.FoodCategory
import me.june.restaurant.exception.CategoryNotFoundException
import me.june.restaurant.repository.FoodCategoryRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class FoodCategoryService(
    private val repository: FoodCategoryRepository,
) {

    fun findCategory(id: Long) = repository.findByIdOrNull(id) ?: throw CategoryNotFoundException()

    fun findCategories(ids: List<Long>) = repository.findAllById(ids)

    @Transactional
    fun createCategory(dto: FoodCategoryDto.CreateRequest): Long {
        val parentCategory = dto.parentId?.let(this::findCategory)
        val entity = dto.toEntity(parentCategory)
        return repository.save(entity).id
    }

    @Transactional
    fun updateCategory(id: Long, dto: FoodCategoryDto.UpdateRequest) {
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

fun FoodCategoryDto.CreateRequest.toEntity(parent: FoodCategory?): FoodCategory {
    return FoodCategory(
        name = this.name,
        parent = parent,
    )
}

fun FoodCategoryDto.UpdateRequest.toEntity(parent: FoodCategory?): FoodCategory {
    return FoodCategory(
        name = this.name,
        parent = parent
    )
}

