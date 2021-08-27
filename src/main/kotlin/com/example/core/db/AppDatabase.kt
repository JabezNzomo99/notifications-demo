package com.example.core.db

import com.example.core.AppConfig
import com.example.push.SubscriberTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import javax.sql.DataSource

object AppDatabase {

    private lateinit var dataSource: DataSource

    fun init() {
        connectionPool()
        migration()
        if (::dataSource.isInitialized) orm(dataSource)
    }

    private fun connectionPool() {
        val config = HikariConfig()
        config.jdbcUrl = AppConfig.DB.jdbc
        config.username = AppConfig.DB.user
        config.password = AppConfig.DB.password
        config.isAutoCommit = false
        config.maximumPoolSize = 8
        config.validate()
        dataSource = HikariDataSource(config)
    }

    private fun migration() {
        val table = "classpath:db/migration/table"
        val include = AppConfig.DB.migrationList.map { path -> "classpath:$path" }
        val locations = arrayOf(table) + include

        val flyway = Flyway.configure()
            .dataSource(dataSource)
            .locations(*locations)
            .load()
        flyway.migrate()
    }
}

fun orm(dataSource: DataSource) {
    Database.connect(dataSource)
    transaction {
        SchemaUtils.createMissingTablesAndColumns(
            SubscriberTable
        )
    }
}
