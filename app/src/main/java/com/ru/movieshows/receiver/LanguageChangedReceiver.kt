package com.ru.movieshows.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

interface ReceiverListener {
    fun update()
}

class LanguageChangedReceiver(
    private val listener: ReceiverListener,
) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val action = intent?.action ?: return
        if (action == Intent.ACTION_LOCALE_CHANGED) {
            listener.update()
        }
    }

}