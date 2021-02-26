package me.june.restaurant.entity

import javax.persistence.*

@Entity
@DiscriminatorValue("food")
class FoodCategory(
        name: String,
        parent: Category? = null,
        children: MutableList<Category> = arrayListOf()
): Category(name, parent, children) {

    fun update(entity: FoodCategory) {
        this.name = entity.name
        this.parent = entity.parent
    }
}