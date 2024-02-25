package com.ru.movieshows.core.presentation.assignable

import com.ru.movieshows.core.presentation.live.LiveValue
import com.ru.movieshows.core.presentation.live.MutableLiveValue

internal class LiveValueAssignable<T>(
    private val liveValue: LiveValue<T>,
) : Assignable<T> {

    override fun setValue(value: T) {
        (liveValue as? MutableLiveValue<T>)?.setValue(value)
    }

}