package com.ru.movieshows.core.presentation.assignable

internal interface Assignable<T> {
    fun setValue(value: T)
}