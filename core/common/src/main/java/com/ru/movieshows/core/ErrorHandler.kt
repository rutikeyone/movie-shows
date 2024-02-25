package com.ru.movieshows.core

interface ErrorHandler {

    fun handleError(exception: Throwable)

    fun getUserHeader(exception: Throwable) : String

    fun getUserMessage(exception: Throwable): String

}