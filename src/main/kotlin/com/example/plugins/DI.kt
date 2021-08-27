package com.example.plugins

import com.example.core.coreModules
import com.example.push.pushModules
import io.ktor.application.*
import org.koin.ktor.ext.koin
import org.koin.logger.SLF4JLogger

fun Application.configureKoin() {
    koin {
        SLF4JLogger()
        modules(coreModules)
        modules(pushModules)
    }
}