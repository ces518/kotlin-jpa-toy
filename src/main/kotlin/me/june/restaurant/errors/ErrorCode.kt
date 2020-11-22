package me.june.restaurant.errors

enum class ErrorCode(
        val status: Int,
        val code: String,
        val message: String,
) {
    USER_NOT_FOUND(404, "-100", "User Not Found"),

    INTERNAL_SERVER_ERROR(500, "-10000", "Internal Server Error"),
    ;
}