package me.june.restaurant.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiImplicitParams
import io.swagger.annotations.ApiOperation
import me.june.restaurant.dto.FamousRestaurantDto
import me.june.restaurant.service.FamousRestaurantQueryService
import me.june.restaurant.service.FamousRestaurantService
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.Link
import org.springframework.hateoas.PagedModel
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import me.june.restaurant.dto.Result

@Api(tags = ["FAMOUS-RESTAURANT-API"])
@RestController
@RequestMapping("/restaurants")
class FamousRestaurantController(
	private val service: FamousRestaurantService,
	private val queryService: FamousRestaurantQueryService,
) {

	@ApiOperation("맛집 목록 조회")
	@ApiImplicitParams(
		ApiImplicitParam(name = "page", value = "페이지 NO", required = false, defaultValue = "0"),
		ApiImplicitParam(name = "size", value = "페이지 사이즈", required = false, defaultValue = "10"),
		ApiImplicitParam(name = "sort", value = "정렬기준", required = false, defaultValue = "id"),
		ApiImplicitParam(name = "direction", value = "정렬 순서", required = false, defaultValue = "DESC"),
	)
	@GetMapping
	fun getRestaurants(
		@PageableDefault(page = 0, size = 10, sort = ["id"], direction = Sort.Direction.DESC) pageable: Pageable,
		assembler: PagedResourcesAssembler<FamousRestaurantDto.Response>
	): PagedModel<EntityModel<FamousRestaurantDto.Response>> {
		return assembler.toModel(queryService.findAll(pageable))
			.apply {
				add(Link.of("/swagger-ui/index.html#/FAMOUS-RESTAURANT-API/getRestaurantsUsingGET").withRel("profile"))
			}
	}

	@ApiOperation("맛집 조회")
	@ApiImplicitParams(
		ApiImplicitParam(name = "id", value = "맛집 아이디", required = true, defaultValue = "1")
	)
	@GetMapping("{id}")
	fun getRestaurant(@PathVariable id: Long): EntityModel<FamousRestaurantDto.Response> {
		return EntityModel.of(queryService.find(id))
			.apply {
				add(Link.of("/swagger-ui/index.html#/FAMOUS-RESTAURANT-API/getRestaurantUsingGET").withRel("profile"))
			}
	}

	@ApiOperation("맛집 생성")
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	fun createRestaurant(
		@RequestBody request: FamousRestaurantDto.CreateRequest
	): EntityModel<FamousRestaurantDto.Response> {
		val savedId = service.create(request)
		return EntityModel.of(queryService.find(savedId))
			.apply {
				add(Link.of("/swagger-ui/index.html#/FAMOUS-RESTAURANT-API/createRestaurantUsingPOST").withRel("profile"))
			}
	}

	@ApiOperation("맛집 수정")
	@ApiImplicitParams(
		ApiImplicitParam(name = "id", value = "맛집 아이디", required = true, defaultValue = "1")
	)
	@PutMapping("{id}")
	fun updateRestaurant(
		@PathVariable id: Long,
		@RequestBody request: FamousRestaurantDto.UpdateRequest
	): EntityModel<FamousRestaurantDto.Response> {
		service.update(id, request)
		return EntityModel.of(queryService.find(id))
			.apply {
				add(Link.of("/swagger-ui/index.html#/FAMOUS-RESTAURANT-API/updateRestaurantUsingPUT").withRel("profile"))
			}
	}

	@ApiOperation("맛집 삭제")
	@ApiImplicitParams(
		ApiImplicitParam(name = "id", value = "맛집 아이디", required = true)
	)
	@DeleteMapping("{id}")
	fun deleteRestaurant(@PathVariable id: Long): EntityModel<Result<Boolean>> {
		service.delete(id)
		return EntityModel.of(Result.SUCCESS).apply {
			add(Link.of("/swagger-ui/index.html#/FAMOUS-RESTAURANT-API/deleteRestaurantUsingDELETE").withRel("profile"))
		}
	}

}