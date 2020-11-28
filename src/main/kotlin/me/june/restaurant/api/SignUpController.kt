package me.june.restaurant.api

import io.swagger.annotations.Api
import me.june.restaurant.dto.SignUpDto
import me.june.restaurant.repository.UserRepository
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.Link
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Api(tags = ["SIGNUP_API"])
@RestController
@RequestMapping("signup")
class SignUpController(
        private val userRepository: UserRepository,
) {

    @PutMapping("check")
    fun checkId(@RequestParam username: String): ResponseEntity<EntityModel<SignUpDto.CheckResponse>> {
        val user = userRepository.findByUsername(username)
        val model = EntityModel.of(SignUpDto.CheckResponse(user == null))
        model.add(Link.of("/swagger-ui/index.html#/SIGNUP_API/checkIdUsingPut").withRel("profile"))
        return ResponseEntity.ok(model)
    }
}