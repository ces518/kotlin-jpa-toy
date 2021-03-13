package me.june.restaurant.exception

import org.springframework.validation.Errors
import java.lang.RuntimeException

class ValidationException(val errors: Errors) : RuntimeException() {
}