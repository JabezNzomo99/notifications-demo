package com.example.core.models

data class ResponseErrors(val errors: Errors) {
    data class Errors(val body: List<String?> = listOf())
}