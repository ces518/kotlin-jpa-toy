package me.june.restaurant.entity

import javax.persistence.Entity
import javax.persistence.Lob
import javax.persistence.Table

@Entity
@Table(name = "famous_restaurants")
class FamousRestaurant(
	var title: String,

	var subTitle: String,

	@Lob
	var description: String,

	var foodCategoryId: Long,

	var regionCategoryId: Long,
) : BaseEntity()