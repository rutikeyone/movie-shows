package com.ru.movieshows.app.presentation.viewmodel.movies

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ru.movieshows.app.model.movies.MoviesRepository
import com.ru.movieshows.app.presentation.viewmodel.BaseViewModel
import com.ru.movieshows.sources.movies.entities.ReviewEntity
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest

@OptIn(ExperimentalCoroutinesApi::class)
class MovieReviewsViewModel @AssistedInject constructor(
    @Assisted private val movieId: Int,
    private val moviesRepository: MoviesRepository,
) : BaseViewModel() {

    val reviews: Flow<PagingData<ReviewEntity>> = languageTagFlow.flatMapLatest {
        val id = movieId.toString()
        moviesRepository.getPagedMovieReview(it, id)
    }.cachedIn(viewModelScope)

    @AssistedFactory
    interface Factory {
        fun create(movieId: Int): MovieReviewsViewModel
    }

}