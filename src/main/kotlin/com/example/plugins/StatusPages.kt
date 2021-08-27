package com.example.plugins

import com.example.core.exceptions.MissingRequestBodyException
import com.example.core.models.ResponseErrors
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import org.valiktor.ConstraintViolationException

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<MissingRequestBodyException> { exception ->
            application.log.error("Missing Request Body Exception", exception.message)
            call.respond(
                status = HttpStatusCode.BadRequest,
                message = ResponseErrors(ResponseErrors.Errors(listOf(exception.message)))
            )
        }

        exception<ConstraintViolationException> { exception ->
            val violations =
                exception.constraintViolations.map { violation -> "${violation.property}:${violation.constraint.name}" }
            call.respond(
                status = HttpStatusCode.UnprocessableEntity,
                message = ResponseErrors(ResponseErrors.Errors(violations.toList()))
            )
        }

        exception<Throwable> { exception ->
            application.log.error("Unhandled exception", exception)
            call.respond(HttpStatusCode.InternalServerError)
        }
    }
}