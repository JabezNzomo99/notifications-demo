package com.example.core

import com.example.core.db.AppDatabase
import com.typesafe.config.ConfigFactory
import io.ktor.config.*
import org.koin.dsl.module

val coreAppConfigModule = module {
    single { provideKtorConfig() }
}


private fun provideKtorConfig(): ApplicationConfig {
    return HoconApplicationConfig(ConfigFactory.load())
}

val coreModules = listOf(coreAppConfigModule)