package com.example.push

interface PushProvider {
    suspend fun subscribeUser(subscriptionRequest: SubscriptionRequest)
    suspend fun pushToUser(pushUserRequest: PushUserRequest)
}

