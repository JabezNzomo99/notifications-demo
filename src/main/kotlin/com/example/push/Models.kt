package com.example.push

import com.example.core.util.ExtendedUUIDEntity
import com.example.core.util.ExtendedUUIDEntityClass
import com.example.core.util.ExtendedUUIDTable
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*
import com.google.firebase.messaging.Notification as FirebaseNotification

data class SubscriptionRequest(
    val userID: UUID,
    val topics: List<String>,
    val platform: String,
    val token: String,
)

data class PushUserRequest(
    val userID: UUID,
    val message: Message,
)

data class Message(
    val notification: Notification? = null,
    val isHighPriority: Boolean? = false,
    val data: MutableMap<String, String?>?,
)

data class Notification(
    val title: String,
    val body: String,
    val image: String?,
)

data class UserSubscriptions (
    val userID: UUID,
    val subscriptions: List<Subscription>,
)

data class UserSubscription (
    val userID: UUID,
    val subscription: Subscription
)

data class Subscription(
    val platform: String,
    var token: String,
)


fun Message.buildNotification(): FirebaseNotification {
    return FirebaseNotification.builder()
        .setBody(this.notification?.body)
        .setTitle(this.notification?.title)
        .setImage(this.notification?.image)
        .build()
}

object SubscriberTable : ExtendedUUIDTable(name = "subscriber", pk = "user_id") {
    val platform = varchar("platform", 150)
    val token = varchar("token", 200)

    override val primaryKey = PrimaryKey(
        id, platform
    )
}

class SubscriberDAO(id: EntityID<UUID>) : ExtendedUUIDEntity(id, SubscriberTable) {
    companion object : ExtendedUUIDEntityClass<SubscriberDAO>(SubscriberTable)

    var token by SubscriberTable.token
    var platform by SubscriberTable.platform
}