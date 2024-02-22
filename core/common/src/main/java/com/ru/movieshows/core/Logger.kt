package com.ru.movieshows.core

import java.lang.Exception

interface Logger {

    fun log(message: String)

    fun err(exception: Throwable, message: String? = null)

}