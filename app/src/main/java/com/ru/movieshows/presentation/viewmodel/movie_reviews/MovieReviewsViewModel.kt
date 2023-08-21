package com.ru.movieshows.presentation.viewmodel.movie_reviews

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ru.movieshows.domain.entity.ReviewEntity
import com.ru.movieshows.domain.repository.MoviesRepository
import com.ru.movieshows.presentation.viewmodel.BaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import java.util.Locale

class MovieReviewsViewModel @AssistedInject constructor(
    @Assisted movieId: Int,
    moviesRepository: MoviesRepository,
) : BaseViewModel() {
    private val currentLanguage get() = Locale.getDefault().toLanguageTag()

    val reviews: Flow<PagingData<ReviewEntity>> =
            moviesRepository.getPagedMovieReview(currentLanguage, movieId.toString())
            .cachedIn(viewModelScope)

    fun refresh() {
    }

    @AssistedFactory
    interface Factory {
        fun create(movieId: Int): MovieReviewsViewModel
    }
}