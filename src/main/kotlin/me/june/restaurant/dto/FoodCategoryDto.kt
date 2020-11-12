package me.june.restaurant.dto

import io.swagger.annotations.ApiModel

class FoodCategoryDto {

    @ApiModel("카테고리 목록 조회 응답")
    data class Response(
            val id: Long,
            val name: String,
            val children: List<Response> = listOf()
    )

    @ApiModel("카테고리 등록 요청")
    data class CreateRequest(
            val name: String,
            val parentId: Long
    )

    @ApiModel("카테고리 수정 요청")
    data class UpdateRequest(
            val name: String,
            val parentId: Long
    )
}