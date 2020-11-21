package me.june.restaurant.api

import io.swagger.annotations.*
import me.june.restaurant.dto.UserDto
import me.june.restaurant.entity.User
import me.june.restaurant.mapper.UserDtoMapper
import me.june.restaurant.repository.UserRepository
import me.june.restaurant.service.UserService
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.Link
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
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
    @ApiImplicitParams(
            ApiImplicitParam(name = "page", value = "페이지 NO", required = false, defaultValue = "0"),
            ApiImplicitParam(name = "size", value = "페이지 사이즈", required = false, defaultValue = "10"),
            ApiImplicitParam(name = "sort", value = "정렬기준", required = false, defaultValue = "id"),
            ApiImplicitParam(name = "direction", value = "정렬 순서", required = false, defaultValue = "DESC"),
    )
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun getUsers(
            @PageableDefault(page = 0, size = 10, sort = ["id"], direction = Sort.Direction.DESC) pageable: Pageable,
            assembler: PagedResourcesAssembler<UserDto.Response>,
    ) {
        val models = assembler.toModel(userRepository.findAll(pageable).map(userDtoMapper::entityToDto))
        models.add(Link.of("/swagger-ui.html#/회원%20API/getUsersUsingGET").withRel("profile"))
        ResponseEntity.ok(models)
    }

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