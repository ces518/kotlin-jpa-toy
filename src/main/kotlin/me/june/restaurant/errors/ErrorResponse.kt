package me.june.restaurant.errors

import org.springframework.validation.BindingResult

class ErrorResponse(
        errorCode: ErrorCode,
        val message: String = errorCode.message,
        val status: Int = errorCode.status.value(),
        val errors: List<FieldError> = listOf(),
        val code: String = errorCode.code,
) {
    companion object {
        fun of(code: ErrorCode, bindingResult: BindingResult) = ErrorResponse(errorCode = code, errors = FieldError.of(bindingResult))
        fun of(code: ErrorCode) = ErrorResponse(errorCode = code)
        fun of(code: ErrorCode, errors: List<FieldError>) = ErrorResponse(errorCode = code, errors = errors)
    }
}

data class FieldError(
        val field: String,
        val value: String,
        val reason: String,
) {
    companion object {
        fun of(field: String, value: String, reason: String) = arrayListOf(FieldError(field, value, reason))
        fun of (bindingResult: BindingResult) =
            bindingResult.fieldErrors
                    .map { FieldError(field = it.field, value = it.rejectedValue?.toString() ?: "", reason = it.defaultMessage ?: "") }
    }
}