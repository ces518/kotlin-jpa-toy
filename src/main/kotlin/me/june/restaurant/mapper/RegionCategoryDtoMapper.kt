package me.june.restaurant.mapper

import me.june.restaurant.dto.RegionCategoryDto
import me.june.restaurant.entity.Category
import me.june.restaurant.entity.RegionCategory
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper(componentModel = "spring")
interface RegionCategoryDtoMapper {
	@Mappings(
		Mapping(source = "parent.id", target = "parentId"),
	)
	fun entityToDto(entity: RegionCategory): RegionCategoryDto.Response


	@Mapping(source = "parent.id", target = "parentId")
	fun categoryToResponse(category: Category): RegionCategoryDto.Response
}