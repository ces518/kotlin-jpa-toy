package me.june.restaurant.dto

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import me.june.restaurant.vo.Gender
import java.time.LocalDate

class SignUpDto {

    @ApiModel("id 중복체크 응답")
    data class CheckResponse(@ApiModelProperty("결과") val joineable: Boolean)

    @ApiModel("회원가입 요청")
    data class Request(
            @ApiModelProperty("비밀번호", example = "asdf")
            val password: String,
            @ApiModelProperty("로그인 ID", example = "ncucu")
            val username: String,
            @ApiModelProperty("사용자 명", example = "엔꾸꾸")
            val name: String,
            @ApiModelProperty("이메일", example = "ncucu.me@kakaocommerce.com")
            val email: String,
            @ApiModelProperty("생일", example = "1994-04-13")
            val birth: LocalDate,
            @ApiModelProperty("성별", example = "MAN")
            val gender: Gender
    )
}