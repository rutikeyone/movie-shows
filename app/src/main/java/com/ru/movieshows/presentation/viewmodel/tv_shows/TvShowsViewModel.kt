package com.ru.movieshows.presentation.viewmodel.tv_shows

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ru.movieshows.domain.entity.TvShowsEntity
import com.ru.movieshows.domain.repository.MoviesRepository
import com.ru.movieshows.domain.repository.TvShowRepository
import com.ru.movieshows.domain.repository.exceptions.AppFailure
import com.ru.movieshows.presentation.screens.tv_show_search.TvShowSearchFragmentDirections
import com.ru.movieshows.presentation.screens.tvs.TvsFragmentDirections
import com.ru.movieshows.presentation.utils.NavigationIntent
import com.ru.movieshows.presentation.utils.publishEvent
import com.ru.movieshows.presentation.utils.share
import com.ru.movieshows.presentation.viewmodel.BaseViewModel
import com.ru.movieshows.presentation.viewmodel.movies.MoviesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
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
        _state.value = TvShowsState.InPending
        try {
            val trendingTvShows = fetchTrendingTvShows()
            val onAirTvShows = fetchOnAirTvShows()
            val topRatedTvShows = fetchTopRatedTvShows()
            val popularTvShows = fetchPopularTvShows()
            _state.value = TvShowsState.Success(trendingTvShows, onAirTvShows, topRatedTvShows, popularTvShows)
        } catch (e: AppFailure) {
            _state.value = TvShowsState.Failure(e.headerResource(), e.errorResource())
        }
    }

    private suspend fun fetchTrendingTvShows(): ArrayList<TvShowsEntity> {
        val fetchTrendingTvShowsResult = tvShowsRepository.getTrendingTvShows(currentLanguage)
        return fetchTrendingTvShowsResult.getOrThrow()
    }

    private suspend fun fetchOnAirTvShows(): ArrayList<TvShowsEntity> {
        val fetchOnAirTvShowsResult = tvShowsRepository.getOnTheAirTvShows(currentLanguage)
        return fetchOnAirTvShowsResult.getOrThrow()
    }

    private suspend fun fetchTopRatedTvShows(): ArrayList<TvShowsEntity> {
        val fetchTopRatedTvShowsResult = tvShowsRepository.getTopRatedTvShows(currentLanguage)
        return fetchTopRatedTvShowsResult.getOrThrow()
    }

    private suspend fun fetchPopularTvShows(): ArrayList<TvShowsEntity> {
        val fetchPopularTvShows = tvShowsRepository.getPopularTvShows(currentLanguage)
        return fetchPopularTvShows.getOrThrow()
    }

    fun navigateToTvShowSearch() {
        val intent = NavigationIntent.To(TvsFragmentDirections.actionTvsFragmentToTvShowSearchFragment())
        navigationEvent.publishEvent(intent)
    }

    fun navigateToTvShowDetails(tvShow: TvShowsEntity) {
        val id = tvShow.id ?: return
        val action = TvsFragmentDirections.actionTvsFragmentToTvShowDetailsFragment(id)
        val intent = NavigationIntent.To(action)
        navigationEvent.publishEvent(intent)
    }
}