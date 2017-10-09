package io.codebandits.datanest.examples.springbootkotlin

import org.jetbrains.exposed.spring.SpringTransactionManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

@Configuration
open class TransactionManagerConfiguration {

    @Bean
    open fun transactionManager(dataSource: DataSource): PlatformTransactionManager = SpringTransactionManager(dataSource)
}
