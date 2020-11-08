package me.june.restaurant.support

import org.springframework.security.test.context.support.WithMockUser

@kotlin.annotation.Retention
@kotlin.annotation.Target(AnnotationTarget.FUNCTION)
@WithMockUser(username = "ncucu", roles = ["ADMIN"])
annotation class WithMockAdmin {
}