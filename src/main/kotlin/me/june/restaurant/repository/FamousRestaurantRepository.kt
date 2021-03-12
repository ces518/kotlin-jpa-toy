package me.june.restaurant.repository

import me.june.restaurant.entity.FamousRestaurant
import org.springframework.data.jpa.repository.JpaRepository

interface FamousRestaurantRepository : JpaRepository<FamousRestaurant, Long>