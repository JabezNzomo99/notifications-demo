package com.example.plugins

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.util.*
import java.util.*

fun Application.configureConversion() {
    install(DataConversion) {
        convert<UUID> {
            decode { values, _ ->
                values.singleOrNull()?.let { value -> UUID.fromString(value) }
            }

            encode { value ->
                when (value) {
                    null -> emptyList()
                    is UUID -> listOf(value.toString())
                    else -> throw DataConversionException("Cannot convert $value as UUID")
                }
            }
        }
    }
}