package me.june.restaurant.mapper

import me.june.restaurant.dto.FoodCategoryDto
import me.june.restaurant.entity.Category
import me.june.restaurant.entity.FoodCategory
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper(componentModel = "spring")
interface FoodCategoryDtoMapper {
    @Mappings(
            Mapping(source = "parent.id", target = "parentId"),
    )
    fun entityToDto(entity: FoodCategory): FoodCategoryDto.Response


    @Mapping(source = "parent.id", target = "parentId")
    fun categoryToResponse(category: Category): FoodCategoryDto.Response
}