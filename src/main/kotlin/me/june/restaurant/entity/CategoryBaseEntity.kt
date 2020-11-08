package me.june.restaurant.entity

import javax.persistence.*

@MappedSuperclass
abstract class CategoryBaseEntity<T>: BaseEntity() {
    var name: String = ""

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    var parent: T? = null

    @OneToMany(mappedBy = "parent")
    var children: MutableList<T> = arrayListOf()

    abstract fun addChildren(child: T)
}