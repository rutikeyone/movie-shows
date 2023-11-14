package com.ru.movieshows.presentation.viewmodel.tv_shows

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ru.movieshows.data.repository.TvShowRepository
import com.ru.movieshows.domain.entity.TvShowsEntity
import com.ru.movieshows.domain.utils.AppFailure
import com.ru.movieshows.presentation.utils.NavigationIntent
import com.ru.movieshows.presentation.utils.publishEvent
import com.ru.movieshows.presentation.utils.share
import com.ru.movieshows.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvShowsViewModel @Inject constructor(
    private val tvShowsRepository: TvShowRepository,
) : BaseViewModel() {
    private val _state = MutableLiveData<TvShowsState>()
    val state = _state.share()

    init {
        fetchTvShowsData()
    }

    fun fetchTvShowsData() = viewModelScope.launch {
        currentLanguage.collect {
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
        val intent = NavigationIntent.toTvShowsSearch()
        navigationEvent.publishEvent(intent)
    }

    fun navigateToTvShowDetails(tvShow: TvShowsEntity) {
        val id = tvShow.id?.toIntOrNull() ?: return
        val intent = NavigationIntent.toTvShowDetails(id)
        navigationEvent.publishEvent(intent)
    }

    fun navigateToAirTvShows() {
        val action = NavigationIntent.toAirTvShows()
        navigationEvent.publishEvent(action)
    }

    fun navigateToTopRatedTvShows() {
        val action = NavigationIntent.toTopRatedTvShows()
        navigationEvent.publishEvent(action)
    }

    fun navigateToPopularTvShows() {
        val action = NavigationIntent.toTopPopularTvShows()
        navigationEvent.publishEvent(action)
    }
}