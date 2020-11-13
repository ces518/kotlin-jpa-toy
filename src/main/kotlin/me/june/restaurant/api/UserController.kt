package me.june.restaurant.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import me.june.restaurant.dto.UserDto
import me.june.restaurant.mapper.UserDtoMapper
import me.june.restaurant.repository.UserRepository
import me.june.restaurant.service.UserService
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@Api(tags = ["회원 API"])
@RestController
@RequestMapping("/users")
class UserController(
        private val userService: UserService,
        private val userRepository: UserRepository,
        private val userDtoMapper: UserDtoMapper
) {

    @ApiOperation("전체 회원 조회")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun getUsers(
            @ApiParam("페이징 요청 정보")
            @PageableDefault(page = 0, size = 10, sort = ["id"], direction = Sort.Direction.DESC) pageable: Pageable
    ) = ResponseEntity.ok(userRepository.findAll(pageable).map(userDtoMapper::entityToDto))

    @ApiOperation("단일 회원 조회")
    @GetMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun getUser(@PathVariable id: Long) = ResponseEntity.ok(userService.findUser(id).let(userDtoMapper::entityToDto))

    @ApiOperation("회원 생성")
    @PostMapping
    fun createUser(@RequestBody dto: UserDto.CreateRequest) = userService.createUser(dto).let {
        ResponseEntity.status(HttpStatus.CREATED).body(userService.findUser(it).let(userDtoMapper::entityToDto))
    }

    @ApiOperation("회원 수정")
    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun updateUser(@PathVariable id: Long, @RequestBody dto: UserDto.UpdateRequest): ResponseEntity<UserDto.Response> {
        userService.updateUser(id, dto)
        return ResponseEntity.ok(userService.findUser(id).let(userDtoMapper::entityToDto))
    }

    @ApiOperation("회원 삭제")
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun deleteUser(@PathVariable id: Long) = ResponseEntity.ok(userService.deleteUser(id))
}