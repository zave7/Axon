package com.cqrs.seoul

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(
    exclude = [
        DataSourceAutoConfiguration::class
        , DataSourceTransactionManagerAutoConfiguration::class
        , HibernateJpaAutoConfiguration::class
    ]
)
class SeoulBankApp

fun main(args: Array<String>) {
    runApplication<SeoulBankApp>(*args)
}