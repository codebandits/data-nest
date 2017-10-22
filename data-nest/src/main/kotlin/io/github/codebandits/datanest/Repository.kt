package io.github.codebandits.datanest

import io.github.codebandits.results.*
import io.github.codebandits.results.adapters.presenceAsResult
import io.github.codebandits.results.adapters.wrapThrowableInResult
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement
import java.lang.IllegalArgumentException
import java.util.*

abstract class Repository<ModelType, in ModelNewType, IdType : Any>(private val table: IdTable<IdType>) {

    fun create(data: ModelNewType): Result<RepositoryFailure, ModelType> {
        return wrapThrowableInResult { table.insertAndGetId { data.insert(it) } }
                .mapError { RepositoryFailure.SaveFailed(exception = it) as RepositoryFailure }
                .flatMap { it.presenceAsResult().mapError { RepositoryFailure.SaveFailed() as RepositoryFailure } }
                .flatMap { getOne(it) }
    }

    fun update(model: ModelType): Result<RepositoryFailure, ModelType> {
        return wrapThrowableInResult { table.update(model.toUniqueSelect()) { model.update(it) } }
                .mapError { RepositoryFailure.SaveFailed(exception = it) as RepositoryFailure }
                .flatMap { wrapThrowableInResult { table.select(model.toUniqueSelect()) }.mapError { RepositoryFailure.NotFound(exception = it) as RepositoryFailure } }
                .flatMap { it.ensureSingle() }
                .flatMap { it.toModel() }
    }

    fun getRow(model: ModelType): Result<RepositoryFailure, ResultRow> {
        return findOneRow(model.toUniqueSelect())
    }

    fun getOne(id: IdType): Result<RepositoryFailure, ModelType> = getOne(EntityID(id, table))

    fun getOne(id: EntityID<IdType>): Result<RepositoryFailure, ModelType> {
        return table.select { table.id eq id }
                .presenceAsResult()
                .mapError { RepositoryFailure.NotFound() as RepositoryFailure }
                .flatMap { it.ensureSingle() }
                .flatMap { it.toModel() }
    }

    fun getAll(): Result<RepositoryFailure, List<ModelType>> {
        return Success<RepositoryFailure, Query>(table.selectAll())
                .flatMap { query -> query.map { it.toModel() }.traverse() }
    }

    fun findOne(where: SqlExpressionBuilder.() -> Op<Boolean>): Result<RepositoryFailure, ModelType> {
        return findOneRow(where)
                .flatMap { it.toModel() }
    }

    fun findAll(where: SqlExpressionBuilder.() -> Op<Boolean>): Result<RepositoryFailure, List<ModelType>> {
        return findAllRows(where)
                .flatMap { resultRows ->
                    resultRows.map { it.toModel() }.traverse()
                }
    }

    fun findOneRow(where: SqlExpressionBuilder.() -> Op<Boolean>): Result<RepositoryFailure, ResultRow> {
        return findAllRows(where)
                .flatMap { it.ensureSingle() }
    }

    fun findAllRows(where: SqlExpressionBuilder.() -> Op<Boolean>): Result<RepositoryFailure, List<ResultRow>> {
        return table.select(where)
                .presenceAsResult()
                .mapError { RepositoryFailure.NotFound() as RepositoryFailure }
                .map { it.toList() }
    }

    abstract protected fun ResultRow.toModel(): Result<RepositoryFailure, ModelType>
    abstract protected fun ModelNewType.insert(insertStatement: InsertStatement<EntityID<IdType>>)
    abstract protected fun ModelType.update(updateStatement: UpdateStatement)
    abstract protected fun ModelType.toUniqueSelect(): SqlExpressionBuilder.() -> Op<Boolean>
}

private fun <T> Iterable<T>.ensureSingle(): Result<RepositoryFailure, T> {
    return wrapThrowableInResult { this.single() }
            .mapError { throwable ->
                when (throwable) {
                    is NoSuchElementException -> RepositoryFailure.NotFound(exception = throwable)
                    is IllegalArgumentException -> RepositoryFailure.MultipleFound(exception = throwable)
                    else -> throw Exception("unexpected exception", throwable)
                }
            }
}

sealed class RepositoryFailure {
    abstract val message: String?
    abstract val exception: Throwable?

    data class NotFound(override val message: String? = null, override val exception: Throwable? = null) : RepositoryFailure()
    data class MultipleFound(override val message: String? = null, override val exception: Throwable? = null) : RepositoryFailure()
    data class SaveFailed(override val message: String? = null, override val exception: Throwable? = null) : RepositoryFailure()
}
