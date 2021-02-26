package me.june.restaurant.dto

import io.swagger.annotations.ApiModel

class FoodCategoryDto {

    @ApiModel("음식 카테고리 응답")
    data class Response(
        val id: Long,
        val name: String,
        val parentId: Long?,
        var children: List<Response>?
    )

    data class CreateRequest(
        val name: String,
        val parentId: Long?
    )

    data class UpdateRequest(
        val name: String,
        val parentId: Long?
    )
}
