package me.june.restaurant.service

import me.june.restaurant.dto.SignUpDto
import me.june.restaurant.mapper.SignUpDtoMapper
import me.june.restaurant.repository.UserRepository
import me.june.restaurant.vo.Password
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class SignUpService(
        private val userRepository: UserRepository,
        private val passwordEncoder: PasswordEncoder,
        private val signUpDtoMapper: SignUpDtoMapper,
) {

    @Transactional
    fun signUp(dto: SignUpDto.Request): Long {
        val (password, username, name, email, birth, gender) = dto
        if (userRepository.findByUsername(username) != null) {
            throw DuplicateUsernameException("$username 은 이미 존재하는 사용자 입니다.")
        }
        val entity = signUpDtoMapper.dtoToEntity(dto).apply {
            this.password = Password(passwordEncoder.encode(this.password.password))
        }
        return userRepository.save(entity).id
    }

}
class DuplicateUsernameException(msg: String): RuntimeException(msg)