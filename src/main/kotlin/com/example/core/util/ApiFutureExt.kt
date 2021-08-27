package com.example.core.util

import com.google.api.core.ApiFuture
import com.google.api.core.ApiFutureCallback
import com.google.api.core.ApiFutures
import com.google.common.util.concurrent.*
import kotlinx.coroutines.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

suspend fun <T> ApiFuture<T>.await(): T = suspendCancellableCoroutine { continuation ->
    ApiFutures.addCallback(this@await, object : ApiFutureCallback<T> {
        override fun onSuccess(result: T) = continuation.resume(result)
        override fun onFailure(throwable: Throwable) = continuation.resumeWithException(throwable)
    }, MoreExecutors.directExecutor())

    continuation.invokeOnCancellation {
        this@await.cancel(true)
    }
}
