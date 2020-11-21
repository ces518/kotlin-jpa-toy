package me.june.restaurant.service

import me.june.restaurant.dto.UserDto
import me.june.restaurant.mapper.UserDtoMapper
import me.june.restaurant.repository.UserRepository
import me.june.restaurant.vo.Password
import me.june.restaurant.config.security.UserAccount
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService (
        private val userRepository: UserRepository,
        private val userDtoMapper: UserDtoMapper,
        private val passwordEncoder: PasswordEncoder
): UserDetailsService {

    @Cacheable(value = ["findUser"], key = "#id")
    fun findUser(id: Long) = userRepository.findByIdOrNull(id)
            ?: throw UserNotFoundException("$id 에 해당하는 유저를 찾을 수 없습니다.")

    @Transactional
    fun createUser(dto: UserDto.CreateRequest): Long {
        val entity = userDtoMapper.dtoToEntity(dto).apply {
            this.password = Password(passwordEncoder.encode(this.password.password))
        }
        return userRepository.save(entity).id
    }


    @Transactional
    @CacheEvict(value = ["findUser"], key = "#id")
    fun updateUser(id: Long, dto:UserDto.UpdateRequest) {
        findUser(id).apply {
            this.password = Password(passwordEncoder.encode(dto.password))
            this.name = dto.name
            this.birth = dto.birth
            this.email = dto.email
            this.gender = dto.gender
        }
    }

    @Transactional
    @CacheEvict(value = ["findUser"], key = "#id")
    fun deleteUser(id: Long) = userRepository.delete(findUser(id))

    override fun loadUserByUsername(username: String) =
            userRepository.findByUsername(username)?.let {
                UserAccount(it)
            } ?: throw UsernameNotFoundException(username)
}

class UserNotFoundException(msg: String): RuntimeException(msg)