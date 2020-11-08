package me.june.restaurant.service

import me.june.restaurant.dto.LoginDto
import me.june.restaurant.entity.User
import me.june.restaurant.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.RuntimeException

@Service
@Transactional(readOnly = true)
class LoginService(
        private val userRepository: UserRepository,
        private val passwordEncoder: PasswordEncoder
) {

    fun loginUser(loginRequest: LoginDto.Request): User {
        val ( username, password ) = loginRequest
        val findUser = userRepository.findByUsername(username)
                ?: throw UserNotFoundException("$username 에 해당하는 유저는 존재하지 않습니다.")
        if (!findUser.password.matchPassword(password, passwordEncoder)) {
            throw PasswordNotMatchedException()
        }
        return findUser
    }
}

class PasswordNotMatchedException: RuntimeException()