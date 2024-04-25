package com.ru.movieshows.movies.domain

import com.ru.movieshows.movies.domain.entities.MoviesPagination
import com.ru.movieshows.movies.domain.repositories.MoviesRepository
import javax.inject.Inject

class GetSimilarMoviesUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository,
) {

    suspend fun execute(
        language: String = "en_US",
        page: Int = 1,
        movieId: Int,
    ): MoviesPagination {
        return moviesRepository.getSimilarMovies(
            language = language,
            movieId = movieId,
            page = page,
        )
    }

}