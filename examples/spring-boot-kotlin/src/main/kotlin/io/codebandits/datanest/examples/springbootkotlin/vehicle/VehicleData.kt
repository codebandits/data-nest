package io.codebandits.datanest.examples.springbootkotlin.vehicle

import io.codebandits.datanest.examples.springbootkotlin.group.Group
import io.codebandits.datanest.examples.springbootkotlin.group.GroupRepository
import io.codebandits.datanest.examples.springbootkotlin.group.GroupTable
import io.github.codebandits.datanest.Repository
import io.github.codebandits.datanest.RepositoryFailure
import io.github.codebandits.results.Result
import io.github.codebandits.results.map
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement

object VehicleTable : IntIdTable() {
    val name: Column<String> = varchar("name", 100).uniqueIndex()
    val group = reference("group", GroupTable)
}

data class Vehicle(
        val name: String,
        val group: Group
)

data class VehicleNew(
        val name: String,
        val group: Group
)

class VehicleRepository : Repository<Vehicle, VehicleNew, Int>(VehicleTable) {

    private val groupRepository = GroupRepository()

    override fun ResultRow.toModel(): Result<RepositoryFailure, Vehicle> {

        return groupRepository.getOne(this[VehicleTable.group])
                .map { group ->
                    Vehicle(
                            name = this[VehicleTable.name],
                            group = group
                    )
                }
    }

    override fun VehicleNew.insert(insertStatement: InsertStatement<EntityID<Int>>) {
        groupRepository.getRow(group)
                .map { groupRow ->
                    insertStatement[VehicleTable.name] = name
                    insertStatement[VehicleTable.group] = groupRow[GroupTable.id]
                }
    }

    override fun Vehicle.update(updateStatement: UpdateStatement) {
        groupRepository.getRow(group)
                .map { groupRow ->
                    updateStatement[VehicleTable.name] = name
                    updateStatement[VehicleTable.group] = groupRow[GroupTable.id]
                }
    }

    override fun Vehicle.toUniqueSelect(): SqlExpressionBuilder.() -> Op<Boolean> = { VehicleTable.name eq name }
}
