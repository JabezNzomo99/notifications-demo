package com.example.push

import com.example.core.exceptions.MissingRequestBodyException
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Route.push() {
    val controller: Controller by inject()

    route("/push") {
        post("/subscribe") {

            val subscriptionRequest = call.receiveOrNull<SubscriptionRequest>()
                ?: throw MissingRequestBodyException("Subscription Request Body Required!")

            controller.subscribeUser(subscriptionRequest)

            call.respond(status = HttpStatusCode.Accepted, message = mapOf("Status" to "Accepted"))
        }

        post("/user") {
            val pushUserRequest = call.receiveOrNull<PushUserRequest>()
                ?: throw MissingRequestBodyException("Push request body required")

            controller.pushToUser(pushUserRequest)

            call.respond(HttpStatusCode.Accepted, mapOf("Status" to "Accepted"))
        }

    }
}