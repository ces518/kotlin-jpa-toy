package me.june.restaurant.support

import com.querydsl.core.types.EntityPath
import com.querydsl.core.types.Expression
import com.querydsl.core.types.dsl.PathBuilder
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.support.JpaEntityInformation
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport
import org.springframework.data.jpa.repository.support.Querydsl
import org.springframework.data.querydsl.SimpleEntityPathResolver
import org.springframework.data.repository.support.PageableExecutionUtils
import org.springframework.util.Assert
import java.util.function.Function
import javax.annotation.PostConstruct
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

open class Querydsl4RepositorySupport(
	private val domainClass: Class<*>
) {
	private lateinit var querydsl: Querydsl
	private lateinit var entityManager: EntityManager
	protected lateinit var queryFactory: JPAQueryFactory

	@PersistenceContext
	protected fun setEntityManager(entityManager: EntityManager) {
		Assert.notNull(entityManager, "EntityManager must not be null!")
		val entityInformation: JpaEntityInformation<*, *> =
			JpaEntityInformationSupport.getEntityInformation(domainClass, entityManager)
		val resolver = SimpleEntityPathResolver.INSTANCE
		val path: EntityPath<*> = resolver.createPath(entityInformation.javaType)
		this.entityManager = entityManager
		querydsl = Querydsl(entityManager, PathBuilder(path.type, path.metadata))
		queryFactory = JPAQueryFactory(entityManager)
	}

	@PostConstruct
	fun validate() {
		Assert.notNull(entityManager, "EntityManager must not be null!")
		Assert.notNull(querydsl, "Querydsl must not be null!")
		Assert.notNull(queryFactory, "QueryFactory must not be null!")
	}

	protected fun getEntityManager(): EntityManager? {
		return entityManager
	}

	protected fun <T> select(expr: Expression<T>): JPAQuery<T> {
		return queryFactory.select(expr)
	}

	protected fun <T> selectFrom(from: EntityPath<T>): JPAQuery<T> {
		return queryFactory.selectFrom(from)
	}

	protected fun <T> applyPagination(
		pageable: Pageable,
		contentQuery: (JPAQueryFactory) -> JPAQuery<*>
	): Page<T> {
		val jpaQuery = contentQuery(queryFactory)
		val content: List<T> = querydsl.applyPagination(pageable, jpaQuery).fetch() as List<T>
		return PageableExecutionUtils.getPage(content, pageable) { jpaQuery.fetchCount() }
	}

	protected fun <T> applyPagination(
		pageable: Pageable,
		contentQuery: (JPAQueryFactory) -> JPAQuery<*>,
		countQuery: (JPAQueryFactory) -> JPAQuery<*>
	): Page<T> {
		val jpaContentQuery = contentQuery(queryFactory)
		val content: List<T> = querydsl.applyPagination(pageable, jpaContentQuery).fetch() as List<T>
		val countResult = countQuery(queryFactory)
		return PageableExecutionUtils.getPage(content, pageable) { countResult.fetchCount() }
	}
}