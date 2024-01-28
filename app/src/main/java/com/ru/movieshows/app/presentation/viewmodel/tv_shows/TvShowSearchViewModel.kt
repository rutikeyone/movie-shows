package com.ru.movieshows.app.presentation.viewmodel.tv_shows

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.ru.movieshows.app.model.tv_shows.TvShowRepository
import com.ru.movieshows.app.presentation.adapters.SimpleAdapterListener
import com.ru.movieshows.app.presentation.screens.tv_shows.TvShowSearchFragmentDirections
import com.ru.movieshows.app.presentation.sideeffects.navigator.Navigator
import com.ru.movieshows.app.presentation.viewmodel.BaseViewModel
import com.ru.movieshows.app.presentation.viewmodel.tv_shows.state.TvShowSearchState
import com.ru.movieshows.app.utils.share
import com.ru.movieshows.sources.tv_shows.entities.TvShowsEntity
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
) : BaseViewModel(), SimpleAdapterListener<TvShowsEntity> {

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
            _trendingTvShowsState.value = tvShows?.second
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

    override fun onClickItem(data: TvShowsEntity) {
        val id = data.id ?: return
        val action = TvShowSearchFragmentDirections.actionTvShowSearchFragmentToTvShowDetailsFragment(id)
        navigator.navigate(action)
    }


    override fun onClickPosition(position: Int) {
        val tvShow = state.value?.trendingTvShows?.getOrNull(position) ?: return
        onClickItem(tvShow)
    }

    @AssistedFactory
    interface Factory {
        fun create(navigator: Navigator): TvShowSearchViewModel
    }

}