package com.ru.movieshows.movies.domain

import com.ru.movieshows.movies.domain.entities.Movie
import com.ru.movieshows.movies.domain.repositories.MoviesRepository
import java.lang.NullPointerException
import javax.inject.Inject

class GetDiscoverMoviesUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository,
) {

    suspend fun execute(
        language: String = "en_US",
        page: Int = 1,
        withGenresId: String?,
    ): List<Movie> {
        if (withGenresId.isNullOrEmpty()) {
            throw NullPointerException()
        }

        return moviesRepository.getDiscoverMovies(
            language = language,
            page = page,
            withGenresId = withGenresId,
        )
    }
}