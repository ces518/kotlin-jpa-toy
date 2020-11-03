package me.june.restaurant.service

import me.june.restaurant.dto.UserDto
import me.june.restaurant.mapper.UserDtoMapper
import me.june.restaurant.repository.UserRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService (
        private val userRepository: UserRepository,
        private val userDtoMapper: UserDtoMapper
){

    @Cacheable(value = ["findUser"], key = "#id")
    fun findUser(id: Long) =
            userRepository.findById(id).orElseThrow{ UserNotFoundException("$id 에 해당하는 유저를 찾을 수 없습니다.") }

    @Transactional
    fun createdUser(dto: UserDto.CreateRequest) =
            userRepository.save(userDtoMapper.dtoToEntity(dto)).id

    @Transactional
    @CacheEvict(value = ["findUser"], key = "#id")
    fun updateUser(id: Long, dto:UserDto.UpdateRequest) {
        findUser(id).apply {
            this.password = dto.password
            this.username = dto.username
            this.birth = dto.birth
            this.email = dto.email
            this.gender = dto.gender
        }
    }

    @Transactional
    @CacheEvict(value = ["findUser"], key = "#id")
    fun deleteUser(id: Long) = userRepository.delete(findUser(id))
}

class UserNotFoundException(msg: String): RuntimeException(msg)