package me.june.restaurant.mapper

import me.june.restaurant.dto.SignUpDto
import me.june.restaurant.entity.User
import me.june.restaurant.vo.Password
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.mapstruct.Named

@Mapper(componentModel = "spring")
interface SignUpDtoMapper {
	@Mappings(
		Mapping(source = "dto.password", target = "password", qualifiedByName = ["password"])
	)
	fun dtoToEntity(dto: SignUpDto.Request): User

	@Named("password")
	@Mapping(source = "value", target = "password")
	fun toPassword(value: String): Password
}