package com.ru.movieshows.movies.domain

import com.ru.movieshows.movies.domain.entities.MovieDetails
import com.ru.movieshows.movies.domain.repositories.MoviesRepository
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository,
) {

    suspend fun execute(language: String = "en_US", movieId: Int): MovieDetails {
        return moviesRepository.getMovieDetails(
            language = language,
            movieId = movieId,
        )
    }

}