package com.ru.movieshows.app.utils.async

import com.ru.movieshows.app.model.Empty
import com.ru.movieshows.app.model.Error
import com.ru.movieshows.app.model.Pending
import com.ru.movieshows.app.model.Result
import com.ru.movieshows.app.model.Success
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

typealias ValueLoader<A, T> = (A) -> T?
typealias ValueListener<T> = (Result<T>) -> Unit

private class ListenerRecord<A,T>(
    val arg: A,
    val listener: ValueListener<T>,
)

class FutureRecord<T>(
    var future: Future<*>?,
    var previousValue: Result<T>? = null,
    var lastValue: Result<T> = Empty(),
    var cancelled: Boolean = false,
)

class LazyListenersSubject<A : Any, T : Any>(
    private val loaderExecutor: ExecutorService = Executors.newSingleThreadExecutor(),
    private val handleExecutor: ExecutorService = Executors.newSingleThreadExecutor(),
    private val loader: ValueLoader<A, T>
) {

    private val listeners = mutableListOf<ListenerRecord<A, T>>()
    private val futures = mutableMapOf<A, FutureRecord<T>>()

    fun addListener(argument: A, listener: ValueListener<T>): Future<*> = handleExecutor.submit {
        val listenerRecord =
            ListenerRecord(argument, listener)
        listeners.add(listenerRecord)
        val futureRecord = futures[argument]
        if(futureRecord == null) {
            execute(argument)
        } else {
            listener.invoke(futureRecord.lastValue)
        }
    }

    fun reloadAll(silentMode: Boolean = false) = handleExecutor.execute {
        futures.forEach { entry ->
            val argument = entry.key
            val record = entry.value
            restart(argument, record, silentMode)
        }
    }

    fun reloadArguments(argument: A, silentMode: Boolean = false) = handleExecutor.execute {
        val record = futures[argument] ?: return@execute
        restart(argument, record, silentMode)
    }

    fun removeListener(argument: A, listener: ValueListener<T>) = handleExecutor.submit {
        listeners.removeAll { it.listener == listener && it.arg == argument }
        if(!listeners.any { it.arg == argument }) {
            cancel(argument)
        }
    }

    fun updateAllValues(newValue: T?) {
        futures.forEach {
            val result = if(newValue == null) Empty() else Success(
                newValue
            )
            it.value.previousValue = it.value.lastValue
            it.value.lastValue = result
            publish(it.key, result)
        }
    }

    fun updateWithArgument(argument: A, loader: ValueLoader<A, T>) {
        val record = futures[argument] ?: return
        val future = loaderExecutor.submit {
            try {
                publishIfNotCancelled(record, argument, Pending())
                val res = loader(argument)
                if(res == null) {
                    publishIfNotCancelled(record, argument,
                        Empty()
                    )
                } else {
                    publishIfNotCancelled(record, argument,
                        Success(res)
                    )
                }
            } catch (e: Exception) {
                publishIfNotCancelled(record, argument, Error(e))
            }
        }
        record.future = future
    }

    fun cancel(argument: A) {
        val record = futures[argument]
        if(record != null) {
            futures.remove(argument)
            record.future?.cancel(true)
        }
    }

    private fun execute(argument: A, silentMode: Boolean = false) {
        val record = FutureRecord<T>(
            null,
            Pending()
        )
        futures[argument] = record
        val future = loaderExecutor.submit {
            try {
                if(!silentMode) publishIfNotCancelled(record, argument,
                    Pending()
                )
                val res = loader(argument)
                if(res == null) {
                    publishIfNotCancelled(record, argument,
                        Empty()
                    )
                } else {
                    publishIfNotCancelled(record, argument,
                        Success(res)
                    )
                }
            } catch (e: Exception) {
                publishIfNotCancelled(record, argument, Error(e))
            }
        }
        record.future = future
    }

    private fun publishIfNotCancelled(record: FutureRecord<T>, argument: A, result: Result<T>) {
        if(record.cancelled) return
        publish(argument, result)
    }

    private fun publish(argument: A, result: Result<T>) {
        futures.filter { it.key ==  argument }.forEach {
            it.value.previousValue = it.value.lastValue
            it.value.lastValue = result
        }

        listeners.filter { it.arg == argument }.forEach {
            it.listener.invoke(result)
        }
    }

    private fun restart(argument: A, record: FutureRecord<T>, silentMode: Boolean) {
        record.cancelled = true
        record.future?.cancel(true)
        execute(argument, silentMode)
    }

    fun futureRecord(argument: A): FutureRecord<T>? {
        return futures[argument]
    }

}