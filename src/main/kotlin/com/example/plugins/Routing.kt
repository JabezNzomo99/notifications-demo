package com.example.plugins

import com.example.push.push
import io.ktor.routing.*
import io.ktor.application.*
import io.ktor.response.*

fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondText("I'm running!\uD83D\uDE80")
        }
        push()
    }
}

