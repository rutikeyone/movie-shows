package com.ru.movieshows.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Locale

open class BaseViewModel: ViewModel(){
    protected val languageTag: String
        get() {
            val defaultLanguage = Locale.getDefault()
            return defaultLanguage.toLanguageTag()
        }

    val languageTagState: LiveData<String>
        get() = _languageTagFlow.asLiveData()


    private val _languageTagFlow = MutableStateFlow(languageTag)
    val languageTagFlow: Flow<String> get() = _languageTagFlow

    fun updateLanguageTag() {
        _languageTagFlow.value = languageTag
    }
}