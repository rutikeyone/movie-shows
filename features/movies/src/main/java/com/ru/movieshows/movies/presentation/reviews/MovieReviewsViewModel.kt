package com.ru.movieshows.movies.presentation.reviews

import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ru.movieshows.core.presentation.BaseViewModel
import com.ru.movieshows.movies.MoviesRouter
import com.ru.movieshows.movies.domain.GetPagedMovieReviewsUseCase
import com.ru.movieshows.movies.domain.entities.Review
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest

class MovieReviewsViewModel @AssistedInject constructor(
    @Assisted private val args: MovieReviewsFragment.Screen,
    private val router: MoviesRouter,
    private val getPagedMovieReviewsUseCase: GetPagedMovieReviewsUseCase,
) : BaseViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val movieReviews: Flow<PagingData<Review>> = languageTagFlow.flatMapLatest { language ->
        getPagedMovieReviewsUseCase.execute(
            language = language,
            id = args.id,
        ).cachedIn(viewModelScope)
    }

    @AssistedFactory
    interface Factory {
        fun create(
            args: MovieReviewsFragment.Screen,
        ): MovieReviewsViewModel
    }

}