package me.june.restaurant.api

import me.june.restaurant.mapper.UserDtoMapper
import me.june.restaurant.repository.UserRepository
import me.june.restaurant.service.UserService
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(
        private val userService: UserService,
        private val userRepository: UserRepository,
        private val userDtoMapper: UserDtoMapper
) {

    @GetMapping
    fun getUsers(@PageableDefault(page = 0, size = 10, sort = ["id"], direction = Sort.Direction.DESC) pageable: Pageable) =
            userRepository.findAll(pageable).map(userDtoMapper::entityToDto)

    @GetMapping("{id}")
    fun getUser(@PathVariable id: Long) = userRepository.findById(id).map(userDtoMapper::entityToDto)
}