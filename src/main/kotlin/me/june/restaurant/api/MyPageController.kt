package me.june.restaurant.api

import me.june.restaurant.entity.User
import me.june.restaurant.mapper.UserDtoMapper
import me.june.restaurant.service.UserService
import me.june.restaurant.support.AuthUser
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/mypage")
class MyPageController(
        private val userService: UserService,
        private val userDtoMapper: UserDtoMapper,
) {

    @GetMapping("info")
    fun getMyInfo(@AuthUser user: User) = userService.findUser(user.id).let(userDtoMapper::entityToDto)
}