package me.june.restaurant.config.security

import org.springframework.security.core.userdetails.User as Account
import me.june.restaurant.entity.User
import org.springframework.security.core.authority.SimpleGrantedAuthority

class UserAccount(
        val user: User
): Account(user.username, user.password.password, listOf(SimpleGrantedAuthority("ROLE_${user.role}")))