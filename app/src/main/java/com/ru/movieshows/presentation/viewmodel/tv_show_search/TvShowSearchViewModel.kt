package com.ru.movieshows.presentation.viewmodel.tv_show_search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.ru.movieshows.domain.repository.TvShowRepository
import com.ru.movieshows.presentation.utils.share
import com.ru.movieshows.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TvShowSearchViewModel @Inject constructor(
    private val tvShowRepository: TvShowRepository,
) : BaseViewModel() {
    private var _query = MutableLiveData("")
    val query = _query.share()

    private val queryValue get() = if(_query.value.isNullOrEmpty()) null else _query.value
    val searchQueryMode: Boolean get() = !_query.value.isNullOrEmpty()

    private var _expanded: Boolean = false
    val expanded get() = _expanded

    val searchMovies = _query.switchMap {
        tvShowRepository.searchPagedMovies(currentLanguage, queryValue)
            .cachedIn(viewModelScope)
    }


    fun setExpanded(value: Boolean) {
        if(value == expanded) return
        _expanded = value
    }

    fun changeQuery(query: String) {
        _query.postValue(query)
    }
}