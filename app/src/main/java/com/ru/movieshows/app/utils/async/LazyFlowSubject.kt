package com.ru.movieshows.app.utils.async

import com.ru.movieshows.app.model.Result
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.runBlocking

typealias SuspendValueLoader<A, T> = suspend (A) -> T

class LazyFlowSubject<A : Any, T : Any>(
    private val loader: SuspendValueLoader<A, T>
) {

    private val lazyListenersSubject =
        LazyListenersSubject<A, T> { arg ->
            runBlocking {
                loader.invoke(arg)
            }
        }

    fun reloadAll(silentMode: Boolean = false) {
        lazyListenersSubject.reloadAll(silentMode)
    }

    fun reloadArgument(argument: A, silentMode: Boolean = false) {
        lazyListenersSubject.reloadArguments(argument, silentMode)
    }

    fun updateAllValues(newValue: T?) {
        lazyListenersSubject.updateAllValues(newValue)
    }

    fun updateWithArgument(argument: A, loader: SuspendValueLoader<A, T>) {
        lazyListenersSubject.updateWithArgument(argument) { arguments ->
            runBlocking {
                loader.invoke(arguments)
            }
        }
    }

    fun futureRecord(argument: A): FutureRecord<T>? {
        return lazyListenersSubject.futureRecord(argument)
    }

    fun listen(argument: A): Flow<Result<T>> = callbackFlow {
        val listener : ValueListener<T> = { result ->
            trySend(result)
        }
        lazyListenersSubject.addListener(argument, listener)
        awaitClose {
            lazyListenersSubject.removeListener(argument, listener)
        }
    }

}