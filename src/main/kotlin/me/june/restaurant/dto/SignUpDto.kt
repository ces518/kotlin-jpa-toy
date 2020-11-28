package me.june.restaurant.dto

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

class SignUpDto {

    @ApiModel("id 중복체크 응답")
    data class CheckResponse(@ApiModelProperty("결과") val joineable: Boolean)
}