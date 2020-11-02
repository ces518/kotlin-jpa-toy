package me.june.restaurant.service

import me.june.restaurant.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService (
        private val userRepository: UserRepository
){

    fun findUser(id: Long) =
            userRepository.findById(id).orElseThrow{ UserNotFoundException("$id 에 해당하는 유저를 찾을 수 없습니다.") }
}

class UserNotFoundException(msg: String): RuntimeException(msg)