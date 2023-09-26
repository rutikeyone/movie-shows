package com.ru.movieshows.presentation.viewmodel.movie_search

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ru.movieshows.domain.entity.MovieEntity
import com.ru.movieshows.domain.repository.MoviesRepository
import com.ru.movieshows.presentation.utils.MutableUnitLiveEvent
import com.ru.movieshows.presentation.utils.publishEvent
import com.ru.movieshows.presentation.utils.share
import com.ru.movieshows.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MovieSearchViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository,
) : BaseViewModel(){
    private val currentLanguage get() = Locale.getDefault().toLanguageTag()

    private var _query = MutableLiveData("")
    val query = _query.share()

    private var _expanded: Boolean = false
    val expanded get() = _expanded

    private val queryValue get() = if(_query.value.isNullOrEmpty()) null else _query.value
    val searchQueryMode: Boolean get() = !_query.value.isNullOrEmpty()

    var searchMovies = _query.switchMap {
        moviesRepository.searchPagedMovies(currentLanguage, queryValue)
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
