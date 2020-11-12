package me.june.restaurant.mapper

import me.june.restaurant.dto.UserDto
import me.june.restaurant.entity.User
import me.june.restaurant.vo.Password
import org.mapstruct.*

@Mapper(componentModel = "spring")
interface UserDtoMapper {
    fun entityToDto(entity: User): UserDto.Response

    @Mappings(
            Mapping(source = "dto", target = "password", qualifiedByName = ["password"])
    )
    fun dtoToEntity(dto: UserDto.CreateRequest): User

    /**
     * 맵스트럭츠 사용시 매핑이 필요한 값 객체가 필요한 경우 별도의 생성 메소드를 사용
     */
    @Named("password")
    @Mapping(source = "passwordValue", target = "password")
    fun toPassword(dto: UserDto.CreateRequest): Password
}