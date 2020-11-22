package me.june.restaurant.errors

import me.june.restaurant.service.UserNotFoundException
import me.june.restaurant.support.logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.lang.RuntimeException

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

    @ExceptionHandler(Exception::class)
    fun exception(e: Exception): ResponseEntity<ErrorResponse> {
        log.error("handle Exception", e)
        return ResponseEntity(ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR)
    }
}