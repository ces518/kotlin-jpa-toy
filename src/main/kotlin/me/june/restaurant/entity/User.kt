package me.june.restaurant.entity

import me.june.restaurant.vo.Gender
import me.june.restaurant.vo.Password
import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "users")
class User(
        @Embedded
        val password: Password,

        val username: String,

        val email: String,

        val birth: LocalDate,

        @Enumerated(EnumType.STRING)
        val gender: Gender,
): BaseEntity()