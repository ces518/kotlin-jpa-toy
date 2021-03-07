package me.june.restaurant.api

import io.swagger.annotations.Api
import me.june.restaurant.config.token.JwtAuthTokenProvider
import me.june.restaurant.dto.LoginDto
import me.june.restaurant.service.LoginService
import me.june.restaurant.support.CookieUtils
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletResponse

@Api(tags = ["LOGIN-API"])
@RestController
class LoginController(
	private val loginService: LoginService,
	private val jwtAuthTokenProvider: JwtAuthTokenProvider
) {

	@PostMapping("login")
	fun login(
		@RequestBody loginRequest: LoginDto.Request,
		response: HttpServletResponse,
	): ResponseEntity<LoginDto.Response> {
		val loginUser = loginService.loginUser(loginRequest)
		val accessToken = CookieUtils.createCookie(
			JwtAuthTokenProvider.ACCESS_TOKEN,
			jwtAuthTokenProvider.createToken(loginUser.id),
			JwtAuthTokenProvider.EXPIRATION_MS.toInt()
		)
		val refreshToken = CookieUtils.createCookie(
			JwtAuthTokenProvider.REFRESH_TOKEN,
			jwtAuthTokenProvider.createRefreshToken(loginUser.id),
			JwtAuthTokenProvider.REFRESH_TOKEN_EXPIRATION_MS.toInt()
		)
		response.addCookie(accessToken)
		response.addCookie(refreshToken)
		return ResponseEntity.ok(LoginDto.Response())
	}
}