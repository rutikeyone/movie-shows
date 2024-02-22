package com.ru.movieshows.core

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeout

suspend fun <T> Flow<Container<T>>.unwrapFirst(timeoutMillis: Long = Core.remoteTimeoutMillis) {
    return withTimeout(timeoutMillis) {
        filterNot { it is Container.Pending }
            .first()
            .unwrap()
    }
}