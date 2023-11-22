package com.ru.movieshows.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ru.movieshows.presentation.utils.MutableLiveEvent
import com.ru.movieshows.presentation.utils.NavigationIntent
import com.ru.movieshows.presentation.utils.ToastIntent
import com.ru.movieshows.presentation.utils.share
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Locale

open class BaseViewModel: ViewModel(){
    protected val languageTag: String get() = Locale.getDefault().toLanguageTag()

    private val _currentLanguage = MutableStateFlow(languageTag)
    val currentLanguage: Flow<String> get() = _currentLanguage
    protected val currentLanguageData get() = currentLanguage.asLiveData()

    protected val navigationEvent = MutableLiveEvent<NavigationIntent>()
    val navigationShareEvent = navigationEvent.share();

    protected val toastEvent = MutableLiveEvent<ToastIntent>()
    val toastShareEvent = toastEvent.share()

    fun updateCurrentLanguage() {
        _currentLanguage.value = languageTag
    }
}