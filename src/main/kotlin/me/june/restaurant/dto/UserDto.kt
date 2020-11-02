package me.june.restaurant.dto

import me.june.restaurant.vo.Gender
import java.time.LocalDateTime

class UserDto {

    data class Response(
            val id: Long,
            val username: String,
            val email: String,
            val birth: String,
            val gender: Gender,
            val createdAt: LocalDateTime
    )
}