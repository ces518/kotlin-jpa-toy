package me.june.restaurant.dto

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

class RegionCategoryDto {

	@ApiModel("지역 카테고리 응답")
	data class Response(
		@ApiModelProperty("카테고리 아이디")
		val id: Long,
		@ApiModelProperty("카테고리 명")
		val name: String,
		@ApiModelProperty("부모 카테고리 아이디")
		val parentId: Long?,
		@ApiModelProperty("자식 카테고리 목록")
		var children: List<RegionCategoryDto.Response>?
	)

	@ApiModel("지역 카테고리 생성 요청")
	data class CreateRequest(
		@ApiModelProperty("카테고리 명")
		val name: String,
		@ApiModelProperty("부모 카테고리 아이디")
		val parentId: Long?
	)

	@ApiModel("지역 카테고리 수정 요청")
	data class UpdateRequest(
		@ApiModelProperty("카테고리 명")
		val name: String,
		@ApiModelProperty("부모 카테고리 아이디")
		val parentId: Long?
	)
}