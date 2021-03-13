package me.june.restaurant.repository.query

import com.querydsl.core.types.dsl.BooleanExpression
import me.june.restaurant.dto.FamousRestaurantDto
import me.june.restaurant.entity.FamousRestaurant
import me.june.restaurant.entity.QFamousRestaurant.famousRestaurant
import me.june.restaurant.mapper.FoodCategoryDtoMapper
import me.june.restaurant.mapper.RegionCategoryDtoMapper
import me.june.restaurant.repository.FoodCategoryRepository
import me.june.restaurant.repository.RegionCategoryRepository
import me.june.restaurant.support.Querydsl4RepositorySupport
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
class FamousRestaurantQueryRepository(
	private val foodCategoryRepository: FoodCategoryRepository,
	private val regionCategoryRepository: RegionCategoryRepository,
	private val foodCategoryDtoMapper: FoodCategoryDtoMapper,
	private val regionCategoryDtoMapper: RegionCategoryDtoMapper,
) : Querydsl4RepositorySupport(FamousRestaurant::class.java) {

	fun findAll(pageable: Pageable, search: FamousRestaurantSearch): Page<FamousRestaurantDto.Response> {
		val page = applyPagination<FamousRestaurant>(pageable) {
			selectFrom(famousRestaurant)
				.where(
					eqFamousCategory(search.foodCategoryId),
					eqRegionCategory(search.regionCategoryId),
					likeTitle(search.title),
					likeSubTitle(search.subTitle),
				)
		}
		
		val foodCategoryIds = page.content.map { it.foodCategoryId }
		val regionCategoryIds = page.content.map { it.regionCategoryId }
		val foodCategoryMap = foodCategoryRepository.findAllById(foodCategoryIds)
			.map { foodCategoryDtoMapper.entityToDto(it) }.associateBy { it.id }
		val regionCategoyMap = regionCategoryRepository.findAllById(regionCategoryIds)
			.map { regionCategoryDtoMapper.entityToDto(it) }.associateBy { it.id }

		val results = page.content.map { entity ->
			FamousRestaurantDto.Response(
				id = entity.id,
				title = entity.title,
				subTitle = entity.subTitle,
				description = entity.description,
				foodCategory = foodCategoryMap[entity.foodCategoryId]!!,
				regionCategory = regionCategoyMap[entity.regionCategoryId]!!,
			)
		}
		return PageImpl(results, pageable, page.totalElements)
	}

	private fun eqFamousCategory(categoryId: Long?): BooleanExpression? {
		return categoryId?.let {
			famousRestaurant.foodCategoryId.eq(categoryId)
		}

	}
	private fun eqRegionCategory(categoryId: Long?): BooleanExpression? {
		return categoryId?.let {
			famousRestaurant.regionCategoryId.eq(categoryId)
		}
	}
	private fun likeTitle(title: String?): BooleanExpression? {
		return title?.let {
			famousRestaurant.title.like(title)
		}
	}
	private fun likeSubTitle(subTitle: String?): BooleanExpression? {
		return subTitle?.let {
			famousRestaurant.subTitle.like(subTitle)
		}
	}
}

data class FamousRestaurantSearch(
	val foodCategoryId: Long?,
	val regionCategoryId: Long?,
	val title: String?,
	val subTitle: String?,
)