package me.june.restaurant.vo

import org.springframework.security.crypto.password.PasswordEncoder
import java.io.Serializable
import javax.persistence.Embeddable

@Embeddable
data class Password(var password: String): Serializable {
    fun matchPassword(password: String, passwordEncoder: PasswordEncoder) =
            passwordEncoder.matches(password, this.password)
}