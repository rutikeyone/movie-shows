package com.ru.movieshows.domain.utils

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class AsyncLoader <T>(
    private val loader: () -> T,
) {
    private var value: T? = null
    private val mutex = Mutex()

    suspend fun get(): T {
        mutex.withLock {
            if(value == null) {
                value = loader()
            }
        }
        return value!!
    }

}