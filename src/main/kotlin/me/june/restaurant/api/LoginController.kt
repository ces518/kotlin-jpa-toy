package me.june.restaurant.api

import me.june.restaurant.config.token.JwtAuthTokenProvider
import me.june.restaurant.dto.LoginDto
import me.june.restaurant.service.LoginService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class LoginController(
        private val loginService: LoginService,
        private val jwtAuthTokenProvider: JwtAuthTokenProvider
) {

    @PostMapping("login")
    fun login(@RequestBody loginRequest: LoginDto.Request): ResponseEntity<LoginDto.Response> {
        val loginUser = loginService.loginUser(loginRequest)
        return ResponseEntity.ok(LoginDto.Response(token = jwtAuthTokenProvider.createToken(loginUser.id)))
    }
}