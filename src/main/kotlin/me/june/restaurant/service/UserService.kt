package me.june.restaurant.service

import me.june.restaurant.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService (
        private val userRepository: UserRepository
){
}