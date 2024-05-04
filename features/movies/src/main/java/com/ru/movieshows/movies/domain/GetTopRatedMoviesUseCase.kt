package com.ru.movieshows.movies.domain

import com.ru.movieshows.movies.domain.entities.Movie
import com.ru.movieshows.movies.domain.repositories.MoviesRepository
import javax.inject.Inject

class GetTopRatedMoviesUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository,
) {

    suspend fun execute(
        language: String = "en_US",
        page: Int = 1,
    ): List<Movie> {
        return moviesRepository.getTopRatedMovies(
            language = language,
            page = page,
        )
    }

}