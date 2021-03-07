package me.june.restaurant.dto

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import me.june.restaurant.vo.Gender
import java.time.LocalDate
import java.time.LocalDateTime

class UserDto {

	@ApiModel("사용자 정보 조회 응답")
	data class Response(
		@ApiModelProperty("회원 ID", example = "1")
		val id: Long,
		@ApiModelProperty("로그인 ID", example = "ncucu")
		val username: String,
		@ApiModelProperty("사용자 명", example = "엔꾸꾸")
		val name: String,
		@ApiModelProperty("이메일", example = "ncucu.me@kakaocommerce.com")
		val email: String,
		@ApiModelProperty("생일")
		val birth: String,
		@ApiModelProperty("성별", example = "MAN")
		val gender: Gender,
		@ApiModelProperty("생성일자")
		val createdAt: LocalDateTime
	)

	@ApiModel("사용자 생성 요청")
	data class CreateRequest(
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

	@ApiModel("사용자 수정 요청")
	data class UpdateRequest(
		@ApiModelProperty("비밀번호", example = "asdf")
		val password: String,
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