package me.june.restaurant.dto

class LoginDto {

    data class Request(
        val username: String,
        val password: String
    )

    data class Response(
        val success: Boolean = true,
        val message: String = "로그인에 성공했습니다."
    )
}