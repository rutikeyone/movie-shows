package com.ru.movieshows.core.presentation

import kotlinx.coroutines.flow.Flow
import java.util.Locale

interface LocaleObserver {

    fun subscribe() : Flow<Locale>

}