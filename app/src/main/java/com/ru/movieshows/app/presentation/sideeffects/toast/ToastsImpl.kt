package com.ru.movieshows.app.presentation.sideeffects.toast

import android.content.Context
import android.widget.Toast
import com.ru.movieshows.app.utils.dispatcher.MainThreadDispatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ToastsImpl @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
    private val dispatcher: MainThreadDispatcher,
) : Toasts {

    override fun toast(message: String) {
        dispatcher.dispatch {
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }
    }

}