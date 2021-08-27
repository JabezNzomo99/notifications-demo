package com.example

import com.example.core.coreModules
import com.example.core.db.orm
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.testing.*
import org.junit.jupiter.api.BeforeEach
import org.koin.core.context.startKoin
import org.koin.test.junit5.AutoCloseKoinTest
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
open class BaseApiTest : AutoCloseKoinTest() {

    companion object {
        @Container
        private val dbContainer: PostgreSQLContainer<*> = PostgreSQLContainer<Nothing>("postgres:13-alpine")
    }

    @BeforeEach
    fun initKoin() {
        startKoin {
            modules(coreModules)
            modules(mockPushModules)
        }

        initDB()
    }

    private fun initDB() {
        dbContainer.start()
        val config = HikariConfig().apply {
            jdbcUrl = dbContainer.jdbcUrl
            username = dbContainer.username
            password = dbContainer.password
            maximumPoolSize = 4
            validate()
        }
        orm(HikariDataSource(config))
    }
}

fun withTestAppBase(test: TestApplicationEngine.() -> Unit) {
    withTestApplication(
        {
            module(testing = true)
        },
        test
    )
}
