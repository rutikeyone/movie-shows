package com.ru.movieshows.presentation.viewmodel.tv_shows

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ru.movieshows.domain.entity.TvShowsEntity
import com.ru.movieshows.domain.repository.MoviesRepository
import com.ru.movieshows.domain.repository.TvShowRepository
import com.ru.movieshows.domain.repository.exceptions.AppFailure
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

    private val currentLanguage get() = Locale.getDefault().toLanguageTag()

    init {
        fetchTvShowsData()
    }

    fun fetchTvShowsData() = viewModelScope.launch {
        _state.value = TvShowsState.InPending
        try {
            val trendingTvShows = fetchTrendingTvShows()
            _state.value = TvShowsState.Success(trendingTvShows)
        } catch (e: AppFailure) {
            _state.value = TvShowsState.Failure(e.headerResource(), e.errorResource())
        }
    }

    private suspend fun fetchTrendingTvShows(): ArrayList<TvShowsEntity> {
        val fetchTrendingTvShowsResult = tvShowsRepository.getTrendingTvShows(currentLanguage)
        return fetchTrendingTvShowsResult.getOrThrow()
    }
}