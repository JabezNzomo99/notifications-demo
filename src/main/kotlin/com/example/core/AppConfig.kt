package com.example.core

import io.ktor.config.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object AppConfig: KoinComponent {
    private val appConfig: ApplicationConfig by inject()

    object DB{
        private val dbConfig = appConfig.config("ktor.database.connection")
        val jdbc = dbConfig.property("jdbc").getString()
        val user = dbConfig.property("user").getString()
        val password = dbConfig.property("password").getString()

        private val migrationConfig = appConfig.config("ktor.database.migration")
        val migrationList = migrationConfig.property("includes").getList()
    }

    object Firebase {
        const val firebaseSA = "/Users/jabez/Desktop/service-account.json"
    }
}