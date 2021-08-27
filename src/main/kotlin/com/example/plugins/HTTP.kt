package com.example.plugins

import io.ktor.features.*
import io.ktor.application.*

fun Application.configureHTTP() {
    install(DefaultHeaders) {
        header("X-Engine", "Ktor") // will send this header with each response
    }

}
