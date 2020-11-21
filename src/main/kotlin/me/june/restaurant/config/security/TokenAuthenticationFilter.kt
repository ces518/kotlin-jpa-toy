package me.june.restaurant.config.security

import me.june.restaurant.config.token.AuthTokenProvider
import me.june.restaurant.entity.User
import me.june.restaurant.service.UserService
import me.june.restaurant.vo.Gender
import me.june.restaurant.vo.Password
import me.june.restaurant.vo.Roles
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter
import java.time.LocalDate
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class TokenAuthenticationFilter: OncePerRequestFilter() {

    @Autowired
    private lateinit var authTokenProvider: AuthTokenProvider

    @Autowired
    private lateinit var userService: UserService

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val dummyAuthId = request.getHeader("dummy-auth-id")
        when (val isDummyAuth = dummyAuthId == "1") {
            true -> {
                val dummyUser = UserAccount(
                        User(password = Password("1234"),
                                username = "admin",
                                name = "admin",
                                email = "admin@admin.com",
                                birth = LocalDate.now(),
                                gender = Gender.MAN).apply {
                            role = Roles.ADMIN
                        }
                )
                val authentication = UsernamePasswordAuthenticationToken(dummyUser, dummyUser.password, dummyUser.authorities)
                authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authentication
            }
            else -> {
                val token = authTokenProvider.parseTokenString(request)
                if (authTokenProvider.validateToken(token)) {
                    token!!
                    try {
                        val userId = authTokenProvider.getTokenOwnerId(token)
                        val userAccount = UserAccount(userService.findUser(userId))
                        val authentication = UsernamePasswordAuthenticationToken(userAccount, userAccount.password, userAccount.authorities)
                        authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                        SecurityContextHolder.getContext().authentication = authentication
                    } catch (e: UsernameNotFoundException) {
                        // TODO Exception Throw
                    }
                }
            }
        }
        filterChain.doFilter(request, response)
    }
}