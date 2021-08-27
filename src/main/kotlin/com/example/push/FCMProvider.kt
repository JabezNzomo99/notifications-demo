package com.example.push

import com.example.core.util.await
import com.google.firebase.messaging.AndroidConfig
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.MulticastMessage
import com.google.firebase.messaging.WebpushConfig
import java.util.*

internal class FCMProvider(private val fcm: FirebaseMessaging, private val repository: Repository) : PushProvider {

    override suspend fun subscribeUser(subscriptionRequest: SubscriptionRequest) {

        subscriptionRequest.topics.forEach { topic ->
            val topicManagementResponse = fcm.subscribeToTopicAsync(
                listOf(subscriptionRequest.token),
                topic
            ).await()

            if (topicManagementResponse.errors.isNotEmpty()) {
                val error = topicManagementResponse.errors.first()
                throw SubscriptionException(error.reason)
            }

            repository.create(subscriptionRequest)
        }
    }

    override suspend fun pushToUser(pushUserRequest: PushUserRequest) {
        val tokens = Cache.get(pushUserRequest.userID)?.subscriptions?.map {
            it.token
        }

        val message = MulticastMessage.builder()
            .addAllTokens(tokens)
            .setMessagePriority(pushUserRequest.message.isHighPriority)
            .setMessageData(pushUserRequest.message.data)
            .setNotification(pushUserRequest.message.buildNotification())
            .build()

        fcm.sendMulticastAsync(message).await()
    }

}

private fun MulticastMessage.Builder.setMessagePriority(isHighPriority: Boolean?): MulticastMessage.Builder {
    if (isHighPriority == true) {
        this.setAndroidConfig(AndroidConfig.builder().setPriority(AndroidConfig.Priority.HIGH).build())
            .setWebpushConfig(WebpushConfig.builder().putAllHeaders(mapOf("Urgency" to "high")).build())
    }

    return this
}

private fun MulticastMessage.Builder.setMessageData(
    data: MutableMap<String, String?>?,
): MulticastMessage.Builder {
    if (!data.isNullOrEmpty()) {
        data.toMutableMap()["messageID"] = UUID.randomUUID().toString()
        this.putAllData(data)
    } else {
        this.putAllData(mapOf("messageID" to UUID.randomUUID().toString()))
    }

    return this
}