package me.june.restaurant.entity

import javax.persistence.*

@Entity
@Table(name = "food_categories")
class FoodCategory: CategoryBaseEntity<FoodCategory>() {
    override fun addChildren(child: FoodCategory) {
        this.children.add(child)
        child.parent = this
    }
}