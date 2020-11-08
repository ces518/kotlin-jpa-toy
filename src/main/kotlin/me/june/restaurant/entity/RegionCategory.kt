package me.june.restaurant.entity

import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "region_category")
class RegionCategory: CategoryBaseEntity<RegionCategory>() {
    override fun addChildren(child: RegionCategory) {
        this.children.add(child)
        child.parent = this
    }
}