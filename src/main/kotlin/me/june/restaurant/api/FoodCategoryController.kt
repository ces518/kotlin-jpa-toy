package me.june.restaurant.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import me.june.restaurant.mapper.FoodCategoryDtoMapper
import me.june.restaurant.repository.FoodCategoryRepository
import org.springframework.web.bind.annotation.*

@Api(tags = ["Food Category API"])
@RestController
@RequestMapping("categories/food")
class FoodCategoryController(
        private val foodCategoryRepository: FoodCategoryRepository,
        private val foodCategoryDtoMapper: FoodCategoryDtoMapper,
) {

    @ApiOperation("카테고리 목록 조회")
    @GetMapping
    fun getCategories() = foodCategoryRepository.findAll().map(foodCategoryDtoMapper::entityToDto)

    @ApiOperation("카테고리 등록")
    @PostMapping
    fun createCategory() {

    }

    @ApiOperation("카테고리 수정")
    @PutMapping("{id}")
    fun updateCategory() {

    }

    @ApiOperation("카테고리 삭제")
    @DeleteMapping("{id}")
    fun deleteCategory() {

    }

}