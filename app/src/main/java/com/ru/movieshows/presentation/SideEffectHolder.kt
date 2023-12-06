package com.ru.movieshows.presentation

import com.ru.movieshows.presentation.sideeffects.navigator.NavigatorWrapper

interface SideEffectHolder {

    fun navigator(): NavigatorWrapper

}