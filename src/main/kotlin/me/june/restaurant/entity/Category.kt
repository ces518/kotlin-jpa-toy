package me.june.restaurant.entity

import javax.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn
abstract class Category(
        var name: String,
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "parent_id")
        var parent: Category? = null,
        @OneToMany(mappedBy = "parent", cascade = [CascadeType.ALL], orphanRemoval = true)
        var children: MutableList<Category> = arrayListOf()
): BaseEntity() {
    fun addChildren(child: Category) {
        // 기존 연관관계 제거
        child.parent?.children?.remove(child)

        child.parent = this
        this.children.add(child)
    }
}