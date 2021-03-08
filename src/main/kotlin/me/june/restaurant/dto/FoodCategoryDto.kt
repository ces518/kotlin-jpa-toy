package me.june.restaurant.dto

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

class FoodCategoryDto {

	@ApiModel("음식 카테고리 응답")
	data class Response(
		@ApiModelProperty("카테고리 아이디")
		val id: Long,
		@ApiModelProperty("카테고리 명")
		val name: String,
		@ApiModelProperty("부모 카테고리 아이디")
		val parentId: Long?,
		@ApiModelProperty("자식 카테고리 목록")
		var children: List<Response>?
	)

	@ApiModel("음식 카테고리 생성 요청")
	data class CreateRequest(
		@ApiModelProperty("카테고리 명")
		override val name: String,
		@ApiModelProperty("부모 카테고리 아이디")
		override val parentId: Long?
	) : AbstractCategoryDto

	@ApiModel("음식 카테고리 수정 요청")
	data class UpdateRequest(
		@ApiModelProperty("카테고리 명")
		override val name: String,
		@ApiModelProperty("부모 카테고리 아이디")
		override val parentId: Long?
	) : AbstractCategoryDto
}
