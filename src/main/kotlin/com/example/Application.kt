package com.example

import com.example.core.db.AppDatabase
import io.ktor.server.netty.*
import com.example.plugins.*
import io.ktor.application.*

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module(testing: Boolean = false) {
    configureKoin()
    configureRouting()
    configureHTTP()
    configureMonitoring()
    configureSerialization()
    configureStatusPages()
    configureConversion()
    if(!testing) AppDatabase.init()
}