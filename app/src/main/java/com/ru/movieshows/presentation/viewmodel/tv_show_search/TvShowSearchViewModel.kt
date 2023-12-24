package com.ru.movieshows.presentation.viewmodel.tv_show_search

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.ru.movieshows.data.repository.TvShowRepository
import com.ru.movieshows.domain.entity.TvShowsEntity
import com.ru.movieshows.presentation.screens.tv_show_search.TvShowSearchFragmentDirections
import com.ru.movieshows.presentation.sideeffects.navigator.Navigator
import com.ru.movieshows.presentation.viewmodel.BaseViewModel
import com.ru.movieshows.presentation.viewmodel.share
import com.ru.movieshows.presentation.viewmodel.tv_show_search.state.TvShowSearchState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class TvShowSearchViewModel @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val tvShowRepository: TvShowRepository,
) : BaseViewModel() {
    val state = MediatorLiveData<TvShowSearchState>()

    private val _trendingTvShowsState = MutableLiveData<ArrayList<TvShowsEntity>?>()

    private var _query = MutableLiveData("")
    val query = _query.share()

    val queryValue get() = if(_query.value.isNullOrEmpty()) null else _query.value
    val isSearchMode: Boolean get() = !_query.value.isNullOrEmpty()

    val searchTvShows = state.asFlow().flatMapLatest {
        tvShowRepository
            .searchPagedMovies(it.language, it.query)
    }.cachedIn(viewModelScope)

    init {
        configureMediatorState()
        collectTrendingTvShows()
    }

    private fun collectTrendingTvShows() = viewModelScope.launch {
        languageTagFlow.collect { language ->
            val fetchTrendingTvShowsResult = tvShowRepository.getTrendingTvShows(
                language = language,
                page = 1,
            )
            val tvShows = fetchTrendingTvShowsResult.getOrNull()
            _trendingTvShowsState.value = tvShows
        }
    }

    private fun configureMediatorState() {
        state.addSource(_query) { query ->
            val languageTag = languageTagState.value ?: return@addSource
            val trendingTvShows = _trendingTvShowsState.value
            state.value = TvShowSearchState(languageTag, query, trendingTvShows, isSearchMode)
        }

        state.addSource(languageTagState) { languageTag ->
            val query = _query.value ?: return@addSource
            val trendingTvShows = _trendingTvShowsState.value
            state.value = TvShowSearchState(languageTag, query, trendingTvShows, isSearchMode)
        }

        state.addSource(_trendingTvShowsState) { trendingTvShows ->
            val languageTag = languageTagState.value ?: return@addSource
            val query = _query.value ?: return@addSource
            state.value = TvShowSearchState(languageTag, query, trendingTvShows, isSearchMode)
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
        navigator.navigate(action)
    }

    @AssistedFactory
    interface Factory {
        fun create(navigator: Navigator): TvShowSearchViewModel
    }

}