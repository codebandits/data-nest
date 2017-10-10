package io.codebandits.datanest.examples.springbootkotlin.vehicle

import io.codebandits.datanest.examples.springbootkotlin.BaseDataTest
import io.codebandits.datanest.examples.springbootkotlin.group.GroupNew
import io.codebandits.datanest.examples.springbootkotlin.group.GroupRepository
import io.codebandits.datanest.examples.springbootkotlin.group.GroupTable
import io.github.codebandits.results.succeedsAnd
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.transaction.annotation.Transactional

open class VehicleDataTest : BaseDataTest(GroupTable, VehicleTable) {

    private val vehicleRepository = VehicleRepository()
    private val groupRepository = GroupRepository()

    @Test
    @Transactional
    open fun `repository can save, update, and retrieve a vehicle`() {

        val ravagerNew = GroupNew(name = "Ravager")

        groupRepository.create(ravagerNew) succeedsAnd { ravagerCreated ->

            val milanoNew = VehicleNew(name = "Milano", group = ravagerCreated)

            vehicleRepository.create(milanoNew) succeedsAnd { milanoCreated ->

                assertThat(milanoCreated).isEqualToComparingFieldByField(milanoNew)

                vehicleRepository.findOne { VehicleTable.name eq "Milano" } succeedsAnd { milanoFound ->

                    assertThat(milanoFound).isEqualTo(milanoCreated)

                    val guardiansNew = GroupNew(name = "Guardians of the Galaxy")

                    groupRepository.create(guardiansNew) succeedsAnd { guardiansCreated ->

                        val milanoModified = milanoCreated.copy(group = guardiansCreated)

                        vehicleRepository.update(milanoModified) succeedsAnd { vehicleUpdated ->

                            assertThat(vehicleUpdated).isEqualTo(milanoModified)
                        }
                    }
                }
            }
        }
    }
}
