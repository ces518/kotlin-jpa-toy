package me.june.restaurant.entity

import me.june.restaurant.vo.Gender
import me.june.restaurant.vo.Password
import me.june.restaurant.vo.Roles
import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "users")
class User(
        @Embedded
        var password: Password,

        @Column(unique = true)
        var username: String,

        var name: String,

        var email: String,

        var birth: LocalDate,

        @Enumerated(EnumType.STRING)
        var gender: Gender
): BaseEntity() {

        @Enumerated(EnumType.STRING)
        var role: Roles = Roles.USER
}