package com.ru.movieshows.presentation.viewmodel.tv_shows

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ru.movieshows.data.repository.TvShowRepository
import com.ru.movieshows.domain.entity.TvShowsEntity
import com.ru.movieshows.domain.utils.AppFailure
import com.ru.movieshows.presentation.screens.tv_shows.TvShowsFragmentDirections
import com.ru.movieshows.presentation.sideeffects.navigator.NavigatorWrapper
import com.ru.movieshows.presentation.viewmodel.share
import com.ru.movieshows.presentation.viewmodel.BaseViewModel
import com.ru.movieshows.presentation.viewmodel.tv_shows.state.TvShowsState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class TvShowsViewModel @AssistedInject constructor(
    @Assisted private val navigator: NavigatorWrapper,
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
                val trendingTvShows = fetchTrendingTvShows(it)
                val onAirTvShows = fetchOnAirTvShows(it)
                val topRatedTvShows = fetchTopRatedTvShows(it)
                val popularTvShows = fetchPopularTvShows(it)
                _state.value = TvShowsState.Success(trendingTvShows, onAirTvShows, topRatedTvShows, popularTvShows)
            } catch (e: AppFailure) {
                _state.value = TvShowsState.Failure(e.headerResource(), e.errorResource())
            }
        }
    }

    private suspend fun fetchTrendingTvShows(language: String): ArrayList<TvShowsEntity> {
        val fetchTrendingTvShowsResult = tvShowsRepository.getTrendingTvShows(language)
        return fetchTrendingTvShowsResult.getOrThrow()
    }

    private suspend fun fetchOnAirTvShows(language: String): ArrayList<TvShowsEntity> {
        val fetchOnAirTvShowsResult = tvShowsRepository.getOnTheAirTvShows(language)
        return fetchOnAirTvShowsResult.getOrThrow()
    }

    private suspend fun fetchTopRatedTvShows(language: String): ArrayList<TvShowsEntity> {
        val fetchTopRatedTvShowsResult = tvShowsRepository.getTopRatedTvShows(language)
        return fetchTopRatedTvShowsResult.getOrThrow()
    }

    private suspend fun fetchPopularTvShows(language: String): ArrayList<TvShowsEntity> {
        val fetchPopularTvShows = tvShowsRepository.getPopularTvShows(language)
        return fetchPopularTvShows.getOrThrow()
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

    @AssistedFactory
    interface Factory {
        fun create(navigator: NavigatorWrapper): TvShowsViewModel
    }

}