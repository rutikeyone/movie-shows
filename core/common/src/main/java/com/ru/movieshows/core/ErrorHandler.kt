package com.ru.movieshows.core

interface ErrorHandler {

    fun handleError(exception: Throwable)

    fun getUserMessage(exception: Throwable): String

}