package me.june.restaurant.mapper

import me.june.restaurant.dto.FamousRestaurantDto
import me.june.restaurant.entity.FamousRestaurant
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface FamousRestaurantDtoMapper {

	fun dtoToEntity(dto: FamousRestaurantDto.CreateRequest): FamousRestaurant

	fun dtoToEntity(dto: FamousRestaurantDto.UpdateRequest): FamousRestaurant
}