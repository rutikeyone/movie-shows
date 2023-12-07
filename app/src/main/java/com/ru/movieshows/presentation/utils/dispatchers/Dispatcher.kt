package com.ru.movieshows.presentation.utils.dispatchers

interface Dispatcher {

    fun dispatch(block: () -> Unit)

}