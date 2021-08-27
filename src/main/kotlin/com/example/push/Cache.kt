package com.example.push

import com.github.benmanes.caffeine.cache.AsyncLoadingCache
import com.github.benmanes.caffeine.cache.Caffeine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.future.await
import kotlinx.coroutines.future.future
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

object Cache : KoinComponent {
    private val cacheContext = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val repository: Repository by inject()

    private val store: AsyncLoadingCache<UUID, Any?> =
        Caffeine.newBuilder()
            .buildAsync { userID, _ ->
                cacheContext.future {
                    repository.getUserSubscriptions(userID)
                }
            }

    suspend fun get(userID: UUID): UserSubscriptions? = withContext(cacheContext.coroutineContext) {
        store[userID].await() as? UserSubscriptions
    }
}