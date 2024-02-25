package com.ru.movieshows.core.presentation.live

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

class Event<T>(
    value: T
) {

    private var _value: T? = value

    fun get(): T? = _value.also { _value = null }

}

typealias LiveEventValue<T> = LiveValue<Event<T>>

fun <T> LiveValue<Event<T>>.observeEvent(lifecycleOwner: LifecycleOwner, observer: (T) -> Unit) {
    observe(lifecycleOwner) { event ->
        val value = event.get() ?: return@observe
        observer(value)
    }
}