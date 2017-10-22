package io.github.codebandits.datanest

import io.github.codebandits.datanest.person.PersonNew
import io.github.codebandits.datanest.person.PersonRepository
import io.github.codebandits.datanest.person.PersonTable
import io.github.codebandits.results.failsWithA
import io.github.codebandits.results.succeedsAnd
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RepositoryTest {

    @BeforeEach
    fun setUp() {
        val database = Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver")
        database.connector
    }

    private val repository = PersonRepository()

    private val gamora = PersonNew(name = "Gamora")
    private val groot = PersonNew(name = "Groot")
    private val starLord = PersonNew(name = "Star-Lord")

    @Test
    fun `create should return the model`() {
        transaction {
            SchemaUtils.create(PersonTable)

            repository.create(starLord) succeedsAnd { createdPerson ->
                assertThat(createdPerson.name).isEqualTo("Star-Lord")
            }
        }
    }

    @Test
    fun `create when save is invalid should fail`() {
        transaction {
            SchemaUtils.create(PersonTable)

            val badGroot = PersonNew(name = "I am Groot! ".repeat(50).trim())
            repository.create(badGroot) failsWithA RepositoryFailure.SaveFailed::class
        }
    }

    @Test
    fun `update should return the model`() {
        transaction {
            SchemaUtils.create(PersonTable)

            repository.create(starLord) succeedsAnd { createdPerson ->

                val modifiedPerson = createdPerson.copy(name = "Mantis")
                repository.update(modifiedPerson) succeedsAnd { updatedPerson ->
                    assertThat(updatedPerson.id).isEqualTo(createdPerson.id)
                    assertThat(updatedPerson.name).isEqualTo("Mantis")
                }
            }
        }
    }

    @Test
    fun `getAll should return all the objects`() {
        transaction {
            SchemaUtils.create(PersonTable)

            repository.create(gamora)
            repository.create(groot)
            repository.create(starLord)

            repository.getAll() succeedsAnd { people ->
                assertThat(people).hasSize(3)
            }
        }
    }

    @Test
    fun `findOne by query when one result is found should return the model`() {
        transaction {
            SchemaUtils.create(PersonTable)

            repository.create(gamora)
            repository.create(groot)
            repository.create(starLord)

            repository.findOne {
                PersonTable.name.regexp("^S")
            } succeedsAnd { person ->
                assertThat(person.name).isEqualTo("Star-Lord")
            }
        }
    }

    @Test
    fun `findOne by query when multiple results are found should fail`() {
        transaction {
            SchemaUtils.create(PersonTable)

            repository.create(gamora)
            repository.create(groot)
            repository.create(starLord)

            repository.findOne {
                PersonTable.name.regexp("^G")
            } failsWithA RepositoryFailure.MultipleFound::class
        }
    }

    @Test
    fun `findOne by query when no results are found should fail`() {
        transaction {
            SchemaUtils.create(PersonTable)

            repository.create(gamora)
            repository.create(groot)
            repository.create(starLord)

            repository.findOne {
                PersonTable.name.eq("Drax")
            } failsWithA RepositoryFailure.NotFound::class
        }
    }

    @Test
    fun `findAll by query should return all matching objects`() {
        transaction {
            SchemaUtils.create(PersonTable)

            repository.create(gamora)
            repository.create(groot)
            repository.create(starLord)

            repository.findAll {
                PersonTable.name.regexp("^G")
            } succeedsAnd { results ->
                assertThat(results).hasSize(2)
            }
        }
    }
}
