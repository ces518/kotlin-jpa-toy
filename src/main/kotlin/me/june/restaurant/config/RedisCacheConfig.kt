package me.june.restaurant.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory

@Configuration
@EnableCaching(proxyTargetClass = true)
class RedisCacheConfig(
	private val redisConnectionFactory: RedisConnectionFactory,
	private val objectMapper: ObjectMapper
) {

//    @Bean
//    override fun cacheManager(): CacheManager? =
//        RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory).apply {
//            objectMapper.registerModule(KotlinModule())
//            objectMapper.enableDefaultTypingAsProperty(ObjectMapper.DefaultTyping.NON_FINAL, "@class")
//
//            val config = RedisCacheConfiguration.defaultCacheConfig()
//                    .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(GenericJackson2JsonRedisSerializer(objectMapper)))
//                    .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer()))
//                    .entryTtl(Duration.ofMinutes(1L))
//            cacheDefaults(config)
//        }.build()
//
//    @Bean
//    override fun cacheManager(): CacheManager = RedisCacheManager(redisTemplate)
}