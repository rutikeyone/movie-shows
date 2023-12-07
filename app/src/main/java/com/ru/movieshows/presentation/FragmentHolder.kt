package com.ru.movieshows.presentation

import com.ru.movieshows.presentation.sideeffects.loader.LoaderOverlay
import com.ru.movieshows.presentation.sideeffects.navigator.NavigatorWrapper
import com.ru.movieshows.presentation.sideeffects.resources.Resources
import com.ru.movieshows.presentation.sideeffects.toast.Toasts

interface FragmentHolder {

    fun navigator(): NavigatorWrapper

    fun toasts(): Toasts

    fun resources(): Resources

    fun loaderOverlay(): LoaderOverlay

}