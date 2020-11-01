package me.june.restaurant.repository

import me.june.restaurant.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long>