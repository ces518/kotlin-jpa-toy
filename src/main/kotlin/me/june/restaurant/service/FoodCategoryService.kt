package me.june.restaurant.service

import me.june.restaurant.dto.FoodCategoryDto
import me.june.restaurant.entity.FoodCategory
import me.june.restaurant.exception.CategoryNotFoundException
import me.june.restaurant.repository.FoodCategoryRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FoodCategoryService(
    private val repository: FoodCategoryRepository,
) {

    @Transactional
    fun createCategory(dto: FoodCategoryDto.CreateRequest): Long {
        val parentCategory = dto.parentId?.let {
            repository.findByIdOrNull(dto.parentId)
                ?: throw CategoryNotFoundException()
        }

        val entity = dto.toEntity(parentCategory).apply {
            parent = parentCategory
        }
        return repository.save(entity).id
    }
}

fun FoodCategoryDto.CreateRequest.toEntity(parent: FoodCategory?): FoodCategory {
    return FoodCategory(
        name = this.name,
        parent = parent,
    )
}

