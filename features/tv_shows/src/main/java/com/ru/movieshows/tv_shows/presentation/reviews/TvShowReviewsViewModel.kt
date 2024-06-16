package com.ru.movieshows.tv_shows.presentation.reviews

import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ru.movieshows.core.presentation.BaseViewModel
import com.ru.movieshows.tv_shows.TvShowsRouter
import com.ru.movieshows.tv_shows.domain.GetPagedTvShowReviewsUseCase
import com.ru.movieshows.tv_shows.domain.entities.Review
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest

class TvShowReviewsViewModel @AssistedInject constructor(
    @Assisted private val args: TvShowReviewsFragment.Screen,
    private val getPagedTvShowReviewsUseCase: GetPagedTvShowReviewsUseCase,
) : BaseViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val tvShowReviews: Flow<PagingData<Review>> = languageTagFlow.flatMapLatest { language ->
        getPagedTvShowReviewsUseCase.execute(
            language = language,
            id = args.id,
        ).cachedIn(viewModelScope)
    }

    @AssistedFactory
    interface Factory {
        fun create(
            args: TvShowReviewsFragment.Screen,
        ): TvShowReviewsViewModel
    }

}