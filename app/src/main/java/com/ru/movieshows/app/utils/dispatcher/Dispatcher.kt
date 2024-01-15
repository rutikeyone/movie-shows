package com.ru.movieshows.app.utils.dispatcher

interface Dispatcher {

    fun dispatch(block: () -> Unit)

}