package me.june.restaurant.support

import org.springframework.security.core.annotation.AuthenticationPrincipal

@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@kotlin.annotation.Target(AnnotationTarget.VALUE_PARAMETER)
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : user")
annotation class AuthUser