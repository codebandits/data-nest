package io.codebandits.datanest.examples.springbootkotlin.vehicle

import io.github.codebandits.datanest.Repository
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
    val groupAffiliation: Column<String> = varchar("group_affiliation", 100)
}

data class Vehicle(
        val name: String,
        val groupAffiliation: String
)

data class VehicleNew(
        val name: String,
        val groupAffiliation: String
)

class VehicleRepository : Repository<Vehicle, VehicleNew, Int>(VehicleTable) {

    override fun ResultRow.toModel(): Vehicle {
        return Vehicle(
                name = this[VehicleTable.name],
                groupAffiliation = this[VehicleTable.groupAffiliation]
        )
    }

    override fun VehicleNew.insert(insertStatement: InsertStatement<EntityID<Int>>) {
        insertStatement[VehicleTable.name] = name
        insertStatement[VehicleTable.groupAffiliation] = groupAffiliation
    }

    override fun Vehicle.update(updateStatement: UpdateStatement) {
        updateStatement[VehicleTable.name] = name
        updateStatement[VehicleTable.groupAffiliation] = groupAffiliation
    }

    override fun Vehicle.selectExisting(): SqlExpressionBuilder.() -> Op<Boolean> = { VehicleTable.name eq name }
}
