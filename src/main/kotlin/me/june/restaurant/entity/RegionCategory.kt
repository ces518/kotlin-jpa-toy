package me.june.restaurant.entity

import javax.persistence.DiscriminatorValue
import javax.persistence.Entity

@Entity
@DiscriminatorValue("region")
class RegionCategory(
        name: String,
        parent: Category? = null,
        children: MutableList<Category> = arrayListOf()
): Category(name, parent, children)