package me.june.restaurant.vo

import java.io.Serializable
import javax.persistence.Embeddable

@Embeddable
data class Password(var password: String): Serializable