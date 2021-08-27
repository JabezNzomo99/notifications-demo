package com.example

import com.example.push.*
import com.google.api.core.SettableApiFuture
import com.google.firebase.messaging.BatchResponse
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.SendResponse
import com.google.firebase.messaging.TopicManagementResponse
import io.kotest.assertions.ktor.shouldHaveStatus
import io.kotest.matchers.shouldNotBe
import io.ktor.http.*
import io.ktor.server.testing.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Test
import org.koin.dsl.module
import java.util.*

val fcmService: FirebaseMessaging = mockk()
val mockFcmModule = module {
    single {
        fcmService
    }
}

val mockPushModules = listOf(
    repositoryModules,
    controllerModules,
    mockFcmModule,
    providerModules
)

class PushApiTest : BaseApiTest() {

    private val topicManagementResponse: TopicManagementResponse = mockk()
    private val batchResponse: BatchResponse = mockk()
    private val sendResponse: SendResponse = mockk()

    @Test
    fun `test whether POST subscribe returns 400 when posted with an empty request body`() =
        withTestAppBase {
            handleRequest(HttpMethod.Post, "/push/subscribe") {
                addHeader(HttpHeaders.ContentType, "application/json")
            }.apply {
                response.shouldHaveStatus(HttpStatusCode.BadRequest)
            }
        }

    @Test
    fun `test whether POST subscribe returns 422 when posted with an invalid body`() =
        withTestAppBase {
            handleRequest(HttpMethod.Post, "/push/subscribe") {
                addHeader(HttpHeaders.ContentType, "application/json")
                setBody(postInvalidSubscriptionRequest)
            }.apply {
                response.shouldHaveStatus(HttpStatusCode.UnprocessableEntity)
            }
        }

    @Test
    fun `test whether POST subscribe returns 202 when posted with an valid SubscriptionRequest body`() {
        every {
            topicManagementResponse.errors
        } returns emptyList()

        every {
            fcmService.subscribeToTopicAsync(any(), any())
        } returns SettableApiFuture.create<TopicManagementResponse?>().apply {
            set(topicManagementResponse)
        }

        val userID = UUID.randomUUID()
        withTestAppBase {
            handleRequest(HttpMethod.Post, "/push/subscribe") {
                addHeader(HttpHeaders.ContentType, "application/json")
                setBody(postValidSubscriptionRequest.replace("$", userID.toString()))
            }.apply {
                response.shouldHaveStatus(HttpStatusCode.Accepted)
            }
        }

        transaction {
            SubscriberDAO.find {
                SubscriberTable.id eq userID
            } shouldNotBe null
        }
    }

    @Test
    fun `test whether POST push to user returns 400 when posted with an empty request body`() {
        withTestAppBase {
            handleRequest(HttpMethod.Post, "/push/user") {
                addHeader(HttpHeaders.ContentType, "application/json")
            }.apply {
                response.shouldHaveStatus(HttpStatusCode.BadRequest)
            }
        }
    }

    @Test
    fun `test whether POST push to user returns 422 when posted with an invalid PushUserRequest`() {
        withTestAppBase {
            handleRequest(HttpMethod.Post, "/push/user") {
                addHeader(HttpHeaders.ContentType, "application/json")
                setBody(invalidPushUserRequest)
            }.apply {
                response.shouldHaveStatus(HttpStatusCode.UnprocessableEntity)
            }
        }
    }

    @Test
    fun `test whether POST user returns 202 when posted with a valid PushUserRequest`() {
        every {
            sendResponse.isSuccessful
        } returns true

        every {
            batchResponse.responses
        } returns listOf(sendResponse)

        every {
            fcmService.sendMulticastAsync(any())
        } returns SettableApiFuture.create<BatchResponse>().apply {
            set(batchResponse)
        }

        val userID = UUID.randomUUID()
        transaction {
            SubscriberDAO.new(userID) {
                token = "test"
                platform = "test-android"
            }
        }
        withTestAppBase {
            handleRequest(HttpMethod.Post, "/push/user") {
                addHeader(HttpHeaders.ContentType, "application/json")
                setBody(validPushUserRequest.replace("$", userID.toString()))
            }.apply {
                response.shouldHaveStatus(HttpStatusCode.Accepted)
            }
        }

        verify {
            fcmService.sendMulticastAsync(any())
        }
    }
}
