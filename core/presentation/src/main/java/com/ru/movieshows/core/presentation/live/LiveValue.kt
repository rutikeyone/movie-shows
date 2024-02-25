package com.ru.movieshows.core.presentation.live

import androidx.lifecycle.LifecycleOwner

interface LiveValue<T> {

    fun observe(lifecycleOwner: LifecycleOwner, listener: (T) -> Unit)

    fun requireValue(): T

    fun getValue(): T?

}