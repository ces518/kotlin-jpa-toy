package me.june.restaurant.config.token

import io.jsonwebtoken.*
import me.june.restaurant.support.logger
import org.springframework.http.HttpHeaders
import org.springframework.util.StringUtils
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*
import javax.servlet.http.HttpServletRequest

class JwtAuthTokenProvider: AuthTokenProvider {

    companion object {
        private val log = logger(JwtAuthToken::class)
        private const val SECRET_KEY = "FOOD_SERVICE"
        private const val EXPIRATION_MS: Long = 1000 * 60 * 60 * 24
    }

    override fun parseTokenString(request: HttpServletRequest) = request.getHeader(HttpHeaders.AUTHORIZATION)?.let {
        if (it.startsWith("Bearer ")) {
            it.substring(7)
        } else null
    }

    override fun issue(id: Long): AuthToken {
        return JwtAuthToken(createToken(id))
    }

    override fun getTokenOwnerId(token: String): Long {
        val claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .body
        return claims.subject.toLong()
    }

    override fun validateToken(token: String?): Boolean {
        if (StringUtils.hasText(token)) {
            try {
                Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token)
                return true
            } catch (e: SignatureException) {
                log.error("Invalid signature, $e")
            } catch (e: MalformedJwtException) {
                log.error("Invalid token, $e")
            } catch (e: ExpiredJwtException) {
                log.error("Expired Token, $e")
            } catch (e: IllegalArgumentException) {
                log.error("JWT claims string is empty $e")
            }
        }
        return false
    }

    fun createToken(id: Long): String {
        val now = LocalDateTime.now()
        val expiredAt = now.plus(EXPIRATION_MS, ChronoUnit.MILLIS)
        return Jwts.builder()
                .setSubject(id.toString())
                .setIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(expiredAt.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact()
    }
}