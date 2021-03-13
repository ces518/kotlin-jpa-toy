package me.june.restaurant.mapper

import me.june.restaurant.dto.FamousRestaurantDto
import me.june.restaurant.dto.RegionCategoryDto
import me.june.restaurant.entity.FamousRestaurant
import me.june.restaurant.entity.FoodCategory
import me.june.restaurant.entity.RegionCategory
import org.mapstruct.*

@Mapper(componentModel = "spring", uses = [FoodCategoryDtoMapper::class, RegionCategoryDtoMapper::class])
interface FamousRestaurantDtoMapper {

	fun dtoToEntity(dto: FamousRestaurantDto.CreateRequest): FamousRestaurant

	fun dtoToEntity(dto: FamousRestaurantDto.UpdateRequest): FamousRestaurant
}