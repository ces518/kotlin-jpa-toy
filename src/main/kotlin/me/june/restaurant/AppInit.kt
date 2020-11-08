package me.june.restaurant

import me.june.restaurant.entity.User
import me.june.restaurant.repository.UserRepository
import me.june.restaurant.vo.Gender
import me.june.restaurant.vo.Password
import me.june.restaurant.vo.Roles
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class AppInit(
        private val userRepository: UserRepository
): ApplicationRunner {

    override fun run(args: ApplicationArguments?) {

        val admin = User(password = Password("asdf"),
                username = "ncucu",
                email = "ncucu.me@kakaocommerce.com",
                birth = LocalDate.of(1994, 4, 13),
                gender = Gender.MAN)
        admin.role = Roles.ADMIN

        userRepository.save(admin)
    }
}