package me.june.restaurant.repository

import me.june.restaurant.entity.RegionCategory
import org.springframework.data.jpa.repository.JpaRepository

interface RegionCategoryRepository : JpaRepository<RegionCategory, Long>