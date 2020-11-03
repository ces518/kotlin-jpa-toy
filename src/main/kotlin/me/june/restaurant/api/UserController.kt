package me.june.restaurant.api

import me.june.restaurant.dto.UserDto
import me.june.restaurant.mapper.UserDtoMapper
import me.june.restaurant.repository.UserRepository
import me.june.restaurant.service.UserService
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(
        private val userService: UserService,
        private val userRepository: UserRepository,
        private val userDtoMapper: UserDtoMapper
) {

    @GetMapping
    fun getUsers(@PageableDefault(page = 0, size = 10, sort = ["id"], direction = Sort.Direction.DESC) pageable: Pageable) =
            ResponseEntity.ok(userRepository.findAll(pageable).map(userDtoMapper::entityToDto))

    @GetMapping("{id}")
    fun getUser(@PathVariable id: Long) = ResponseEntity.ok(userService.findUser(id).let(userDtoMapper::entityToDto))

    @PostMapping
    fun createUser(@RequestBody dto: UserDto.CreateRequest) = userService.createdUser(dto).let {
        ResponseEntity.status(HttpStatus.CREATED).body(userService.findUser(it).let(userDtoMapper::entityToDto))
    }

    @PutMapping("{id}")
    fun updateUser(@PathVariable id: Long, @RequestBody dto: UserDto.UpdateRequest): ResponseEntity<UserDto.Response> {
        userService.updateUser(id, dto)
        return ResponseEntity.ok(userService.findUser(id).let(userDtoMapper::entityToDto))
    }

    @DeleteMapping("{id}")
    fun deleteUser(@PathVariable id: Long) = ResponseEntity.ok(userService.deleteUser(id))
}