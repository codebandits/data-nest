package io.codebandits.datanest.examples.springbootkotlin.group

import io.github.codebandits.datanest.Repository
import io.github.codebandits.datanest.RepositoryFailure
import io.github.codebandits.results.Result
import io.github.codebandits.results.Success
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement

object GroupTable : IntIdTable() {
    val name: Column<String> = varchar("name", 100).uniqueIndex()
}

data class Group(
        val name: String
)

data class GroupNew(
        val name: String
)

class GroupRepository : Repository<Group, GroupNew, Int>(GroupTable) {

    override fun ResultRow.toModel(): Result<RepositoryFailure, Group> {
        return Success(Group(
                name = this[GroupTable.name]
        ))
    }

    override fun GroupNew.insert(insertStatement: InsertStatement<EntityID<Int>>) {
        insertStatement[GroupTable.name] = name
    }

    override fun Group.update(updateStatement: UpdateStatement) {
        updateStatement[GroupTable.name] = name
    }

    override fun Group.toUniqueSelect(): SqlExpressionBuilder.() -> Op<Boolean> = { GroupTable.name eq name }
}
