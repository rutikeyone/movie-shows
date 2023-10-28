package com.ru.movieshows.presentation.viewmodel.tv_show_search

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.ru.movieshows.data.repository.TvShowRepository
import com.ru.movieshows.domain.entity.TvShowsEntity
import com.ru.movieshows.presentation.screens.tv_show_search.TvShowSearchFragmentDirections
import com.ru.movieshows.presentation.utils.NavigationIntent
import com.ru.movieshows.presentation.utils.publishEvent
import com.ru.movieshows.presentation.utils.share
import com.ru.movieshows.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TvShowSearchViewModel @Inject constructor(
    private val tvShowRepository: TvShowRepository,
) : BaseViewModel() {
    private val state = MediatorLiveData<Pair<String, String>>()

    private var _query = MutableLiveData("")
    val query = _query.share()

    val queryValue get() = if(_query.value.isNullOrEmpty()) null else _query.value
    val searchQueryMode: Boolean get() = !_query.value.isNullOrEmpty()

    val searchMovies = state.switchMap {
        tvShowRepository.searchPagedMovies(it.second, queryValue)
            .cachedIn(viewModelScope)
    }

    init {
        setupMediatorState()
    }

    private fun setupMediatorState() {
        state.addSource(_query) { query ->
            val languageTag = currentLanguageData.value
            if (languageTag != null) {
                state.value = Pair(query, languageTag)
            }
        }

        state.addSource(currentLanguageData) { languageTag ->
            val query = _query.value
            if (query != null) {
                state.value = Pair(query, languageTag)
            }
        }
    }

    fun changeQuery(query: String) {
        if(queryValue != query) {
            _query.postValue(query)
        }
    }

    fun navigateToTvShowDetails(tvShow: TvShowsEntity) {
        val id = tvShow.id ?: return
        val action = TvShowSearchFragmentDirections.actionTvShowSearchFragmentToTvShowDetailsFragment(id)
        val intent = NavigationIntent.To(action)
        navigationEvent.publishEvent(intent)
    }
}