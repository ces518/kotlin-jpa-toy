package me.june.restaurant.dto

class Result<T>(
	val value: T
) {
	companion object {
		val SUCCESS = Result(true)
	}
}