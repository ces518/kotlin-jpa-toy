package me.june.restaurant.support

import org.slf4j.LoggerFactory

inline fun <reified T> logger(clazz: T) = LoggerFactory.getLogger(T::class.java)