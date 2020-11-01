package me.june.restaurant.vo

import javax.persistence.Embeddable

@Embeddable
class Password(
        var password: String
)