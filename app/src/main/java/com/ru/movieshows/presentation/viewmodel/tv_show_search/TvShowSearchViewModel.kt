package com.ru.movieshows.presentation.viewmodel.tv_show_search

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.ru.movieshows.data.repository.TvShowRepository
import com.ru.movieshows.domain.entity.TvShowsEntity
import com.ru.movieshows.presentation.utils.share
import com.ru.movieshows.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TvShowSearchViewModel @Inject constructor(
    private val tvShowRepository: TvShowRepository,
) : BaseViewModel() {
    private val state = MediatorLiveData<Pair<String, String>>()

    private var _query = MutableLiveData("")
    val query = _query.share()

    val queryValue get() = if(_query.value.isNullOrEmpty()) null else _query.value
    val searchQueryMode: Boolean get() = !_query.value.isNullOrEmpty()

    val searchTvShows = state.asFlow().flatMapLatest {
        tvShowRepository
            .searchPagedMovies(it.second, it.first)
    }.cachedIn(viewModelScope)

    init {
        setupMediatorState()
    }

    private fun setupMediatorState() {
        state.addSource(_query) { query ->
            val languageTag = languageTagState.value ?: return@addSource
            state.value = Pair(query, languageTag)
        }

        state.addSource(languageTagState) { languageTag ->
            val query = _query.value ?: return@addSource
            state.value = Pair(query, languageTag)
        }
    }

    fun changeQuery(query: String) {
        if(queryValue != query) {
            _query.postValue(query)
        }
    }

    fun navigateToTvShowDetails(tvShow: TvShowsEntity) {
        //TODO
//        val id = tvShow.id?.toIntOrNull() ?: return
//        val action = NavigationIntent.toTvShowDetails(id)
//        navigationEvent.publishEvent(action)
    }
}