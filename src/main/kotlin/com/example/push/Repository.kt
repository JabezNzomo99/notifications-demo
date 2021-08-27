package com.example.push

import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.*

internal class Repository {

    suspend fun create(subscriptionRequest: SubscriptionRequest) = newSuspendedTransaction {
        newSuspendedTransaction {
            SubscriberDAO.new(subscriptionRequest.userID) {
                token = subscriptionRequest.token
                platform = subscriptionRequest.platform
            }
        }
    }

    suspend fun getUserSubscriptions(userID: UUID): UserSubscriptions = newSuspendedTransaction {
        val subscriptions = SubscriberDAO.find {
            (SubscriberTable.id eq userID)
        }
        if (subscriptions.empty()) throw UserSubscriptionsNotFoundException("User $userID has no subscriptions")
        subscriptions.toUserSubscriptions()
    }
}

fun Iterable<SubscriberDAO>.toUserSubscriptions(): UserSubscriptions {
    return UserSubscriptions(
        userID = this.first().uid,
        subscriptions = this.map { subscriberDAO ->
            Subscription(
                platform = subscriberDAO.platform,
                token = subscriberDAO.token,
            )
        },
    )
}