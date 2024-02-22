package com.ru.movieshows.impl

import android.util.Log
import com.ru.movieshows.core.Logger

class AndroidLogger: Logger {

    override fun log(message: String) {
        Log.d("AndroidLogger", message)
    }

    override fun err(exception: Throwable, message: String?) {
        Log.d("AndroidLogger", message, exception)
    }

}