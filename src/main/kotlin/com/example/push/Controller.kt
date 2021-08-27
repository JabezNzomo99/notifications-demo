package com.example.push

internal class Controller(private val push: PushProvider) {

    suspend fun subscribeUser(subscriptionRequest: SubscriptionRequest) {
        subscriptionRequest.validate()
        push.subscribeUser(subscriptionRequest)
    }

    suspend fun pushToUser(pushUserRequest: PushUserRequest) {
        pushUserRequest.validate()
        push.pushToUser(pushUserRequest)
    }

}