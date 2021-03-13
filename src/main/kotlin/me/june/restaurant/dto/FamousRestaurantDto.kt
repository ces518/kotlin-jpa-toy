package me.june.restaurant.dto

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

class FamousRestaurantDto {

	data class Response(
		val id: Long,
		val title: String,
		val subTitle: String,
		val description: String,
		var foodCategory: FoodCategoryDto.Response,
		var regionCategory: RegionCategoryDto.Response,
	)

	@ApiModel("맛집 등록 요청")
	data class CreateRequest(
		@ApiModelProperty("맛집 명")
		val title: String,
		@ApiModelProperty("맛집 부제목")
		val subTitle: String,
		@ApiModelProperty("맛집 설명")
		val description: String,
		@ApiModelProperty("음식 카테고리 아이디")
		val foodCategoryId: Long,
		@ApiModelProperty("지역 카테고리 아이디")
		val regionCategoryId: Long,
	)

	@ApiModel("맛집 수정 요청")
	data class UpdateRequest(
		@ApiModelProperty("맛집 명")
		val title: String,
		@ApiModelProperty("맛집 부제목")
		val subTitle: String,
		@ApiModelProperty("맛집 설명")
		val description: String,
		@ApiModelProperty("음식 카테고리 아이디")
		val foodCategoryId: Long,
		@ApiModelProperty("지역 카테고리 아이디")
		val regionCategoryId: Long,
	)
}