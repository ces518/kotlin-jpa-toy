package me.june.restaurant.support

import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import kotlin.math.max

object CookieUtils {

	fun createCookie(key: String, value: String, maxAge: Int) =
		Cookie(key, value).apply {
			this.isHttpOnly = true
			this.maxAge = maxAge
			this.path = "/"
		}

	fun getCookie(request: HttpServletRequest, key: String) = request.cookies?.find { it.name == key }
}

