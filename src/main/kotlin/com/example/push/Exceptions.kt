package com.example.push

data class SubscriptionException(override val message: String) : RuntimeException()

data class UserSubscriptionsNotFoundException(override val message: String?): RuntimeException()
