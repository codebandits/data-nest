package io.github.codebandits.datanest.person

import io.github.codebandits.datanest.Repository
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement

object PersonTable : IntIdTable() {
    val name: Column<String> = varchar("name", 100)
}

data class Person(
        val id: Int,
        val name: String
)

data class PersonNew(
        val name: String
)

class PersonRepository : Repository<Person, PersonNew, Int>(PersonTable) {

    override fun Person.toUniqueSelect(): SqlExpressionBuilder.() -> Op<Boolean> = { PersonTable.id eq id }

    override fun PersonNew.insert(insertStatement: InsertStatement<EntityID<Int>>) {
        insertStatement[PersonTable.name] = this.name
    }

    override fun Person.update(updateStatement: UpdateStatement) {
        updateStatement[PersonTable.name] = this.name
    }

    override fun ResultRow.toModel(): Person {
        return Person(
                id = this[PersonTable.id].value,
                name = this[PersonTable.name]
        )
    }
}
