package me.june.restaurant.mapper

import me.june.restaurant.dto.UserDto
import me.june.restaurant.entity.User
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface UserDtoMapper {
    fun entityToDto(entity: User): UserDto.Response
    fun dtoToEntity(dto: UserDto.CreateRequest): User
}