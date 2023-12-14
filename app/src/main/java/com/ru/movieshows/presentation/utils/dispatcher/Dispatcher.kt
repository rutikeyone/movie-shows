package com.ru.movieshows.presentation.utils.dispatcher

interface Dispatcher {
    fun dispatch(block: () -> Unit)
}