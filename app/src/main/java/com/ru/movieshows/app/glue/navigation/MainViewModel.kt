package com.ru.movieshows.app.glue.navigation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Locale
import javax.inject.Inject

class MainViewModel @Inject constructor() : ViewModel() {

    private val _localeFlow: MutableStateFlow<Locale> = MutableStateFlow(Locale.getDefault())
    val localeFlow: StateFlow<Locale> = _localeFlow

    fun notifyLocaleChanges(locale: Locale) {
        _localeFlow.value = locale
    }

}