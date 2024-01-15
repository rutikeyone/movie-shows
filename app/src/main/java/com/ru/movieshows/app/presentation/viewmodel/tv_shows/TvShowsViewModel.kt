package com.ru.movieshows.app.presentation.viewmodel.tv_shows

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import arrow.core.getOrElse
import com.ru.movieshows.app.model.AppFailure
import com.ru.movieshows.app.model.tv_shows.TvShowRepository
import com.ru.movieshows.app.presentation.screens.tv_shows.TvShowsFragmentDirections
import com.ru.movieshows.app.presentation.sideeffects.navigator.Navigator
import com.ru.movieshows.app.presentation.viewmodel.BaseViewModel
import com.ru.movieshows.app.presentation.viewmodel.tv_shows.state.TvShowsState
import com.ru.movieshows.app.utils.share
import com.ru.movieshows.sources.tv_shows.entities.TvShowsEntity
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class TvShowsViewModel @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val tvShowsRepository: TvShowRepository,
) : BaseViewModel() {

    private val _state = MutableLiveData<TvShowsState>()
    val state = _state.share()

    init {
        fetchTvShowsData()
    }

    fun fetchTvShowsData() = viewModelScope.launch {
        languageTagFlow.collect {
            _state.value = TvShowsState.InPending
            try {
                val trendingTvShows = fetchTrendingTvShows(it).second
                val onAirTvShows = fetchOnAirTvShows(it).second
                val topRatedTvShows = fetchTopRatedTvShows(it).second
                val popularTvShows = fetchPopularTvShows(it).second
                _state.value = TvShowsState.Success(trendingTvShows, onAirTvShows, topRatedTvShows, popularTvShows)
            } catch (e: AppFailure) {
                _state.value = TvShowsState.Failure(e.headerResource(), e.errorResource())
            }
        }
    }

    private suspend fun fetchTrendingTvShows(language: String): Pair<Int, ArrayList<TvShowsEntity>> {
        val fetchTrendingTvShowsResult = tvShowsRepository.getTrendingTvShows(language)
        return fetchTrendingTvShowsResult.getOrElse {
            throw it
        }
    }

    private suspend fun fetchOnAirTvShows(language: String): Pair<Int, ArrayList<TvShowsEntity>> {
        val fetchOnAirTvShowsResult = tvShowsRepository.getOnTheAirTvShows(language)
        return fetchOnAirTvShowsResult.getOrElse {
            throw it
        }
    }

    private suspend fun fetchTopRatedTvShows(language: String): Pair<Int, ArrayList<TvShowsEntity>> {
        val fetchTopRatedTvShowsResult = tvShowsRepository.getTopRatedTvShows(language)
        return fetchTopRatedTvShowsResult.getOrElse {
            throw it
        }
    }

    private suspend fun fetchPopularTvShows(language: String): Pair<Int, ArrayList<TvShowsEntity>> {
        val fetchPopularTvShows = tvShowsRepository.getPopularTvShows(language)
        return fetchPopularTvShows.getOrElse {
            throw it
        }
    }

    fun navigateToTvShowSearch() {
        val action = TvShowsFragmentDirections.actionTvsFragmentToTvShowSearchFragment()
        navigator.navigate(action)
    }

    fun navigateToTvShowDetails(tvShow: TvShowsEntity) {
        val id = tvShow.id ?: return
        val action = TvShowsFragmentDirections.actionTvsFragmentToTvShowDetailsFragment(id)
        navigator.navigate(action)
    }

    fun navigateToAirTvShows() {
        val action = TvShowsFragmentDirections.actionTvsFragmentToAirTvShowsFragment()
        navigator.navigate(action)
    }

    fun navigateToTopRatedTvShows() {
        val action = TvShowsFragmentDirections.actionTvsFragmentToTopRatedTvShowsFragment()
        navigator.navigate(action)
    }

    fun navigateToPopularTvShows() {
        val action = TvShowsFragmentDirections.actionTvsFragmentToPopularTvShowsFragment()
        navigator.navigate(action)
    }

    override fun onCleared() {
        super.onCleared()
    }

    @AssistedFactory
    interface Factory {
        fun create(navigator: Navigator): TvShowsViewModel
    }

}