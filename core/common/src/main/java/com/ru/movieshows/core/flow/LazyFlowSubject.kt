package com.ru.movieshows.core.flow

import com.ru.movieshows.core.Container
import kotlinx.coroutines.flow.Flow

typealias ValueLoader<T> = suspend () -> T

interface LazyFlowSubject<T> {

    fun getLastValue(): Container<T>

    fun listen(): Flow<Container<T>>

    suspend fun newLoad(silently: Boolean = false, valueLoader: ValueLoader<T>? = null): T

    fun newAsyncLoad(silently: Boolean = false, valueLoader: ValueLoader<T>? = null)

    fun updateWith(container: Container<T>)

    fun updateWith(updater: (Container<T>) -> Container<T>)

}