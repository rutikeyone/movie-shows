package com.ru.movieshows.presentation.viewmodel.air_tv_shows

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ru.movieshows.data.repository.TvShowRepository
import com.ru.movieshows.domain.entity.TvShowsEntity
import com.ru.movieshows.presentation.screens.air_tv_shows.AirTvShowsFragmentDirections
import com.ru.movieshows.presentation.sideeffects.navigator.NavigatorWrapper
import com.ru.movieshows.presentation.viewmodel.BaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest

class AirTvShowsViewModel @AssistedInject constructor(
    @Assisted private val navigator: NavigatorWrapper,
    private val tvShowRepository: TvShowRepository,
) : BaseViewModel() {

    val airTvShows: Flow<PagingData<TvShowsEntity>> = languageTagFlow.flatMapLatest {
        tvShowRepository
        .getPagedTheAirTvShows(it)
    }.cachedIn(viewModelScope)

    fun navigateToTvShowDetails(tvShow: TvShowsEntity) {
        val id = tvShow.id ?: return
        val action = AirTvShowsFragmentDirections.actionAirTvShowsFragmentToTvShowDetailsFragment(id)
        navigator.navigate(action)
    }

    @AssistedFactory
    interface Factory {
        fun create(navigator: NavigatorWrapper): AirTvShowsViewModel
    }

}