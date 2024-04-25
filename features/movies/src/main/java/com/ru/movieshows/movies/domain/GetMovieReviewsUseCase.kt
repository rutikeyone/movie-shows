package com.ru.movieshows.movies.domain

import com.ru.movieshows.movies.domain.entities.ReviewsPagination
import com.ru.movieshows.movies.domain.repositories.MoviesRepository
import javax.inject.Inject

class GetMovieReviewsUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository,
) {

    suspend fun execute(
        language: String = "en_US",
        movieId: Int,
        pageIndex: Int = 1,
    ): ReviewsPagination {
        return moviesRepository.getMovieReviews(
            language = language,
            movieId = movieId,
            pageIndex = pageIndex,
        )
    }

}