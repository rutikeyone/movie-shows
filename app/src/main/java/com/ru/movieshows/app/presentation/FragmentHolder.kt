package com.ru.movieshows.app.presentation

import com.ru.movieshows.app.presentation.sideeffects.loader.LoaderOverlay
import com.ru.movieshows.app.presentation.sideeffects.navigator.Navigator
import com.ru.movieshows.app.presentation.sideeffects.resources.Resources
import com.ru.movieshows.app.presentation.sideeffects.toast.Toasts

interface FragmentHolder {

    fun navigator(): Navigator

    fun toasts(): Toasts

    fun resources(): Resources

    fun loaderOverlay(): LoaderOverlay

}