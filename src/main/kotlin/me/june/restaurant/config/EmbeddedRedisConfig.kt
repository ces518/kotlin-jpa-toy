package me.june.restaurant.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories
import redis.embedded.RedisServer
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

@Configuration
@EnableRedisRepositories
class EmbeddedRedisConfig(
	@Value("\${spring.redis.port}")
	val redisPort: Int,
	@Value("\${spring.redis.host}")
	val redisHost: String
) {

	lateinit var redisServer: RedisServer

	@PostConstruct
	fun startRedis() {
		redisServer = RedisServer(redisPort)
		redisServer.start()
	}

	@PreDestroy
	fun stopRedis() {
		redisServer.stop()
	}

	@Bean
	fun redisConnectionFactory(): RedisConnectionFactory =
		LettuceConnectionFactory(redisHost, redisPort)

	@Bean
	fun redisTemplate() = RedisTemplate<Any, Any>().apply {
		setConnectionFactory(redisConnectionFactory())
	}
}