package me.june.restaurant.mapper

import me.june.restaurant.dto.FoodCategoryDto
import me.june.restaurant.entity.FoodCategory
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface FoodCategoryDtoMapper {
    fun entityToDto(entity: FoodCategory): FoodCategoryDto.Response
}