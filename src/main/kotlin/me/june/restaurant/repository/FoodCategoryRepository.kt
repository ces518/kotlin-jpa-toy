package me.june.restaurant.repository

import me.june.restaurant.entity.FoodCategory
import org.springframework.data.jpa.repository.JpaRepository

interface FoodCategoryRepository: JpaRepository<FoodCategory, Long>