package com.ru.movieshows.presentation.viewmodel.movie_search

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleObserver
import com.ru.movieshows.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieSearchViewModel @Inject constructor() : BaseViewModel(){
    private var _expanded: Boolean = false
    val expanded get() = _expanded

    fun setExpanded(value: Boolean) {
        if(value == expanded) return
        _expanded = value
    }
}