package me.june.restaurant.errors

import org.springframework.http.HttpStatus

enum class ErrorCode(
	val status: HttpStatus,
	val code: String,
	val message: String,
) {
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "-100", "User Not Found"),

	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "-10000", "Internal Server Error"),

	DUPLICATE_USER_NAME(HttpStatus.CONFLICT, "-10001", "Duplicate Username"),

	CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "-20001", "Category NotFound"),
	;
}