package me.june.restaurant.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiImplicitParams
import io.swagger.annotations.ApiOperation
import me.june.restaurant.dto.FoodCategoryDto
import me.june.restaurant.mapper.FoodCategoryDtoMapper
import me.june.restaurant.repository.FoodCategoryRepository
import me.june.restaurant.service.FoodCategoryService
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.Link
import org.springframework.hateoas.PagedModel
import org.springframework.web.bind.annotation.*
import me.june.restaurant.dto.Result
import org.springframework.http.HttpStatus

@Api(tags = ["FOOD-CATEGORY-API"])
@RestController
@RequestMapping("/categories/food")
class FoodCategoryController(
	private val foodCategoryRepository: FoodCategoryRepository,
	private val foodCategoryService: FoodCategoryService,
	private val foodCategoryDtoMapper: FoodCategoryDtoMapper,
) {

	@ApiOperation("카테고리 목록 조회")
	@ApiImplicitParams(
		ApiImplicitParam(name = "page", value = "페이지 NO", required = false, defaultValue = "0"),
		ApiImplicitParam(name = "size", value = "페이지 사이즈", required = false, defaultValue = "10"),
		ApiImplicitParam(name = "sort", value = "정렬기준", required = false, defaultValue = "id"),
		ApiImplicitParam(name = "direction", value = "정렬 순서", required = false, defaultValue = "DESC"),
	)
	@GetMapping
	fun getCategories(
		@PageableDefault(page = 0, size = 10, sort = ["id"], direction = Sort.Direction.DESC) pageable: Pageable,
		assembler: PagedResourcesAssembler<FoodCategoryDto.Response>
	): PagedModel<EntityModel<FoodCategoryDto.Response>> {
		val models = assembler.toModel(foodCategoryRepository.findAll(pageable).map(foodCategoryDtoMapper::entityToDto))
		models.add(Link.of("/swagger-ui/index.html#/FOOD-CATEGORY-API/getCategoriesUsingGET").withRel("profile"))
		return models
	}

	@ApiOperation("카테고리 조회")
	@ApiImplicitParams(
		ApiImplicitParam(name = "id", value = "카테고리 아이디", required = false, defaultValue = "1")
	)
	@GetMapping("{id}")
	fun getCategory(@PathVariable id: Long): EntityModel<FoodCategoryDto.Response> {
		val response = foodCategoryService.findCategory(id)
			.let(foodCategoryDtoMapper::entityToDto)
		val model = EntityModel.of(response)
		model.add(Link.of("/swagger-ui/index.html#/FOOD-CATEGORY-API/getCategoryUsingGET").withRel("profile"))
		return model
	}

	@ApiOperation("카테고리 등록")
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	fun createCategory(@RequestBody request: FoodCategoryDto.CreateRequest): EntityModel<FoodCategoryDto.Response> {
		val savedId = foodCategoryService.createCategory(request)
		val response = foodCategoryService.findCategory(savedId)
			.let(foodCategoryDtoMapper::entityToDto)
		val model = EntityModel.of(response)
		model.add(Link.of("/swagger-ui/index.html#/FOOD-CATEGORY-API/createCategoryUsingPOST").withRel("profile"))
		return model
	}

	@ApiOperation("카테고리 수정")
	@ApiImplicitParams(
		ApiImplicitParam(name = "id", value = "카테고리 아이디", required = false, defaultValue = "1")
	)
	@PutMapping("{id}")
	fun updateCategory(
		@PathVariable id: Long,
		@RequestBody request: FoodCategoryDto.UpdateRequest
	): EntityModel<FoodCategoryDto.Response> {
		foodCategoryService.updateCategory(id, request)
		val response = foodCategoryService.findCategory(id)
			.let(foodCategoryDtoMapper::entityToDto)
		val model = EntityModel.of(response)
		model.add(Link.of("/swagger-ui/index.html#/FOOD-CATEGORY-API/updateCategoryUsingPUT").withRel("profile"))
		return model
	}

	@ApiOperation("카테고리 삭제")
	@ApiImplicitParams(
		ApiImplicitParam(name = "id", value = "카테고리 아이디", required = false, defaultValue = "1")
	)
	@DeleteMapping("{id}")
	fun deleteCategory(@PathVariable id: Long): EntityModel<Result<Boolean>> {
		foodCategoryService.deleteCategory(id)
		val model = EntityModel.of(Result.SUCCESS)
		model.add(Link.of("/swagger-ui/index.html#/FOOD-CATEGORY-API/deleteCategoryUsingDELETE").withRel("profile"))
		return model
	}
}