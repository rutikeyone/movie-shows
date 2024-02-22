package com.ru.movieshows.core

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

typealias ScreenResultListener<T> = (T) -> Unit

interface ScreenCommunication {

    fun <T : Any> registerListener(clazz: Class<T>, listener: ScreenResultListener<T>)

    fun <T : Any> unregisterListener(listener: ScreenResultListener<T>)

    fun <T : Any> publishResult(result: T)

}

fun <T : Any> ScreenCommunication.listen(clazz: Class<T>): Flow<T> = callbackFlow {
    val listener: ScreenResultListener<T> = { result ->
        trySend(result)
    }
    registerListener(clazz, listener)
    awaitClose {
        unregisterListener(listener)
    }
}