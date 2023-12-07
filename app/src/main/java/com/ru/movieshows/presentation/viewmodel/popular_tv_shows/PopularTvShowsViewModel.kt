package com.ru.movieshows.presentation.viewmodel.popular_tv_shows

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
class PopularTvShowsViewModel @Inject constructor(
    tvShowRepository: TvShowRepository,
) : BaseViewModel() {

    val popularTvShows: Flow<PagingData<TvShowsEntity>> = languageTagFlow.flatMapLatest {
        tvShowRepository
        .getPagedPopularTvShows(it)
    }.cachedIn(viewModelScope)

    fun navigateToTvShowDetails(tvShow: TvShowsEntity) {
//        val id = tvShow.id?.toIntOrNull() ?: return
//        val action = NavigationIntent.toTvShowDetails(id)
//        navigationEvent.publishEvent(action)
    }

}