package me.june.restaurant.config.token

import javax.servlet.http.HttpServletRequest

interface AuthTokenProvider {

    fun parseTokenString(request: HttpServletRequest): String?

    fun issue(id: Long): AuthToken

    fun getTokenOwnerId(token: String): Long

    fun validateToken(token: String?): Boolean
}