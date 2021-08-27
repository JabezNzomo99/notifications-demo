package com.example.push

import org.valiktor.ConstraintViolationException
import org.valiktor.functions.isNotEmpty
import org.valiktor.functions.isNotNull
import org.valiktor.functions.validate

@Throws(ConstraintViolationException::class)
fun SubscriptionRequest.validate() {
    org.valiktor.validate(this) {
        validate(SubscriptionRequest::userID).apply {
            isNotNull()
        }

        validate(SubscriptionRequest::topics).apply {
            isNotNull()
            isNotEmpty()
        }

        validate(SubscriptionRequest::token).apply {
            isNotNull()
            isNotEmpty()
        }

        validate(SubscriptionRequest::platform).apply {
            isNotNull()
            isNotEmpty()
        }
    }

}

fun PushUserRequest.validate() {
    org.valiktor.validate(this) {
        validate(PushUserRequest::userID).apply {
            isNotNull()
        }
    }

    this.message.validate()
}

fun Message.validate() {
    org.valiktor.validate(this) {
        validate(Message::notification).isNotNull()

        validate(Message::notification).validate {
            validate(Notification::title).apply {
                isNotNull()
                isNotEmpty()
            }

            validate(Notification::body).apply {
                isNotNull()
                isNotEmpty()
            }
        }
    }
}