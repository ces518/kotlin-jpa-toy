package me.june.restaurant.errors

import me.june.restaurant.exception.CategoryNotFoundException
import me.june.restaurant.exception.ValidationException
import me.june.restaurant.service.DuplicateUsernameException
import me.june.restaurant.service.UserNotFoundException
import me.june.restaurant.support.logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandlerController {

	companion object {
		private val log = logger(ExceptionHandlerController::class)
	}

	@ExceptionHandler(UserNotFoundException::class, UsernameNotFoundException::class)
	fun userNotFoundException(e: RuntimeException): ResponseEntity<ErrorResponse> {
		log.error("handle userNotFoundException", e)
		return ResponseEntity(ErrorResponse.of(ErrorCode.USER_NOT_FOUND), HttpStatus.NOT_FOUND)
	}

	@ExceptionHandler(DuplicateUsernameException::class)
	fun duplicateUsernameException(e: DuplicateUsernameException): ResponseEntity<ErrorResponse> {
		log.error("handle Duplicate Username Exception", e)
		return ResponseEntity(ErrorResponse.of(ErrorCode.DUPLICATE_USER_NAME), HttpStatus.CONFLICT)
	}

	@ExceptionHandler(CategoryNotFoundException::class)
	fun categoryNotFoundException(e: CategoryNotFoundException): ResponseEntity<ErrorResponse> {
		log.error("handle CategoryNotFoundException", e)
		return ResponseEntity(ErrorResponse.of(ErrorCode.CATEGORY_NOT_FOUND), HttpStatus.NOT_FOUND)
	}

	@ExceptionHandler(ValidationException::class)
	fun validationException(e: ValidationException): ResponseEntity<ErrorResponse> {
		log.error("handle ValidationException", e)
		return ResponseEntity(ErrorResponse.of(ErrorCode.VALIDATION_ERROR, e.errors), HttpStatus.BAD_REQUEST)
	}

	@ExceptionHandler(Exception::class)
	fun exception(e: Exception): ResponseEntity<ErrorResponse> {
		log.error("handle Exception", e)
		return ResponseEntity(ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR)
	}
}