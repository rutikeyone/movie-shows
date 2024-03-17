package com.ru.movieshows.app.utils.dispatcher

import android.os.Handler
import android.os.Looper
import javax.inject.Inject

class MainThreadDispatcher @Inject constructor() : Dispatcher {

    private val handler = Handler(Looper.getMainLooper())

    override fun dispatch(block: () -> Unit) {
        if (Looper.getMainLooper().thread.id == Thread.currentThread().id) {
            block()
        } else {
            handler.post(block)
        }
    }

}