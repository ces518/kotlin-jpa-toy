package me.june.restaurant.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import me.june.restaurant.dto.SignUpDto
import me.june.restaurant.dto.UserDto
import me.june.restaurant.mapper.UserDtoMapper
import me.june.restaurant.repository.UserRepository
import me.june.restaurant.service.SignUpService
import me.june.restaurant.service.UserService
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.Link
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Api(tags = ["SIGNUP_API"])
@RestController
@RequestMapping("signup")
class SignUpController(
        private val userRepository: UserRepository,
        private val signUpService: SignUpService,
        private val userService: UserService,
        private val userDtoMapper: UserDtoMapper,
) {

    @ApiOperation("ID 중복 검사")
    @PutMapping("check")
    fun checkId(@RequestParam username: String): ResponseEntity<EntityModel<SignUpDto.CheckResponse>> {
        val user = userRepository.findByUsername(username)
        val model = EntityModel.of(SignUpDto.CheckResponse(user == null))
        model.add(Link.of("/swagger-ui/index.html#/SIGNUP_API/checkIdUsingPut").withRel("profile"))
        return ResponseEntity.ok(model)
    }

    @ApiOperation("회원 가입")
    @PostMapping
    fun signUp(@RequestBody dto: SignUpDto.Request) = signUpService.signUp(dto).let {
        val model = EntityModel.of(userService.findUser(it).let(userDtoMapper::entityToDto))
        model.add(linkTo(SignUpController::class.java).slash("info").withRel("query-info"))
        model.add(Link.of("/swagger-ui/index.html#/SIGNUP_API/signUpUsingPost").withRel("profile"))
        ResponseEntity.status(HttpStatus.CREATED).body(model)
    }
}