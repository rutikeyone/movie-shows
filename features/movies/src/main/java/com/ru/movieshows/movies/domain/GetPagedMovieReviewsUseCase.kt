package com.ru.movieshows.movies.domain

import androidx.paging.PagingData
import com.ru.movieshows.movies.domain.entities.Review
import com.ru.movieshows.movies.domain.repositories.MoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPagedMovieReviewsUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository,
) {

    fun execute(
        language: String = "en_US",
        id: String,
    ): Flow<PagingData<Review>> {
        return moviesRepository.getPagedMovieReviews(
            language = language,
            id = id,
        )
    }

}