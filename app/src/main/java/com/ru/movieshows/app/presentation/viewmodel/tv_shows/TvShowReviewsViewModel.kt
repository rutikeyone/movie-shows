package com.ru.movieshows.app.presentation.viewmodel.tv_shows

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ru.movieshows.app.model.tvshows.TvShowRepository
import com.ru.movieshows.app.presentation.viewmodel.BaseViewModel
import com.ru.movieshows.sources.movies.entities.ReviewEntity
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest

@OptIn(ExperimentalCoroutinesApi::class)
class TvShowReviewsViewModel @AssistedInject constructor(
    @Assisted private val tvShowId: String,
    private val tvShowRepository: TvShowRepository,
) : BaseViewModel() {

    val reviews: Flow<PagingData<ReviewEntity>> = languageTagFlow.flatMapLatest {
        tvShowRepository.getPagedTvReviews(it, tvShowId)
    }.cachedIn(viewModelScope)

    @AssistedFactory
    interface Factory {
        fun create(tvShowId: String): TvShowReviewsViewModel
    }

}