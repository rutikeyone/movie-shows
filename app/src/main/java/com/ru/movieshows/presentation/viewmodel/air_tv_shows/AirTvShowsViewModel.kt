package com.ru.movieshows.presentation.viewmodel.air_tv_shows

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ru.movieshows.data.repository.TvShowRepository
import com.ru.movieshows.domain.entity.TvShowsEntity
import com.ru.movieshows.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class AirTvShowsViewModel @Inject constructor(
    tvShowRepository: TvShowRepository,
) : BaseViewModel() {

    val airTvShows: Flow<PagingData<TvShowsEntity>> = languageTagFlow.flatMapLatest {
        tvShowRepository
        .getPagedTheAirTvShows(it)
    }.cachedIn(viewModelScope)

    fun navigateToTvShowDetails(tvShow: TvShowsEntity) {
        //TODO
//        val id = tvShow.id?.toIntOrNull() ?: return
//        val intent = NavigationIntent.toTvShowDetails(id)
//        navigationEvent.publishEvent(intent)
    }

}