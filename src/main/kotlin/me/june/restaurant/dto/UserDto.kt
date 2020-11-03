package me.june.restaurant.dto

import me.june.restaurant.vo.Gender
import me.june.restaurant.vo.Password
import java.time.LocalDate
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

    data class CreateRequest(
            val password: Password,
            val username: String,
            val email: String,
            val birth: LocalDate,
            val gender: Gender
    )

    data class UpdateRequest(
            val password: Password,
            val username: String,
            val email: String,
            val birth: LocalDate,
            val gender: Gender
    )
}