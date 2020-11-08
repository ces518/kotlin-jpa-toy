package me.june.restaurant.entity

import javax.persistence.Entity
import javax.persistence.Lob
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "famous_restaurants")
class FamousRestaurant(
        val title: String,

        val subTitle: String,

        @Lob
        val description: String,

        @ManyToOne
        val foodCategory: FoodCategory,

        @ManyToOne
        val regionCategory: RegionCategory
): BaseEntity()