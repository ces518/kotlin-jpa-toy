package me.june.restaurant.api

import me.june.restaurant.repository.UserRepository
import me.june.restaurant.service.UserService
import org.springframework.cache.annotation.Cacheable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(
        private val userService: UserService,
        private val userRepository: UserRepository
) {

    @GetMapping
    @Cacheable(value = ["getUsers"])
    fun getUsers() = userRepository.findAll()
}