package com.ru.movieshows.core.presentation

import androidx.fragment.app.Fragment

fun Fragment.localeObserver(): LocaleObserver {
    val activity = requireActivity()

    if(activity is LocaleObserver) {
        return activity
    } else {
        throw IllegalStateException("Activity must implement LocaleObserver interface")
    }
}