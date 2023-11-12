package com.ru.movieshows.presentation.viewmodel.air_tv_shows

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ru.movieshows.data.repository.TvShowRepository
import com.ru.movieshows.domain.entity.TvShowsEntity
import com.ru.movieshows.presentation.screens.air_tv_shows.AirTvShowsFragmentDirections
import com.ru.movieshows.presentation.utils.NavigationIntent
import com.ru.movieshows.presentation.utils.publishEvent
import com.ru.movieshows.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class AirTvShowsViewModel @Inject constructor(
    tvShowRepository: TvShowRepository,
) : BaseViewModel() {

    val airTvShows: Flow<PagingData<TvShowsEntity>> = currentLanguage.flatMapLatest {
        tvShowRepository
        .getPagedTheAirTvShows(it)
    }.cachedIn(viewModelScope)

    fun navigateToTvShowDetails(tvShow: TvShowsEntity) {
        val id = tvShow.id?.toString() ?: return
        val directions = AirTvShowsFragmentDirections
        val action = directions.actionAirTvShowsFragmentToTvShowDetailsFragment(id)
        val intent = NavigationIntent.To(action)
        navigationEvent.publishEvent(intent)
    }

}