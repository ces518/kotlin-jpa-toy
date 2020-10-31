package me.june.restaurant

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class RestaurantApplication

fun main(args: Array<String>) {
    runApplication<RestaurantApplication>(*args)
}