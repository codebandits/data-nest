package io.codebandits.datanest.examples.springbootkotlin

import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
abstract class BaseDataTest(private vararg val tables: Table) {

    @BeforeEach
    fun setUp() = SchemaUtils.create(*tables)

    @AfterEach
    fun tearDown() = SchemaUtils.drop(*tables)
}
