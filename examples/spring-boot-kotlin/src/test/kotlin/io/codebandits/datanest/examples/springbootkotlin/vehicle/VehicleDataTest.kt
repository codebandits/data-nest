package io.codebandits.datanest.examples.springbootkotlin.vehicle

import io.codebandits.datanest.examples.springbootkotlin.BaseDataTest
import io.github.codebandits.results.succeedsAnd
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.transaction.annotation.Transactional

open class VehicleDataTest : BaseDataTest(VehicleTable) {

    private val vehicleRepository = VehicleRepository()

    @Test
    @Transactional
    open fun `repository can save, update, and retrieve a vehicle`() {
        val vehicleNew = VehicleNew(name = "Milano", groupAffiliation = "Ravager")

        vehicleRepository.create(vehicleNew) succeedsAnd { vehicleCreated ->

            assertThat(vehicleCreated).isEqualToComparingFieldByField(vehicleNew)

            vehicleRepository.findOne { VehicleTable.name eq "Milano" } succeedsAnd { vehicleFound ->

                assertThat(vehicleFound).isEqualTo(vehicleCreated)

                val vehicleModified = vehicleCreated.copy(groupAffiliation = "Guardians of the Galaxy")

                vehicleRepository.update(vehicleModified) succeedsAnd { vehicleUpdated ->

                    assertThat(vehicleUpdated).isEqualTo(vehicleModified)
                }
            }
        }
    }
}
