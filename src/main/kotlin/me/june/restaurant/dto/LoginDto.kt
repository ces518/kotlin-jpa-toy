package me.june.restaurant.dto

class LoginDto {

    data class Request(
            val username: String,
            val password: String
    )

    data class Response(
            val success: Boolean = true,
            val token: String
    )
}