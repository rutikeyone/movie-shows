package com.ru.movieshows.movies.domain

import com.ru.movieshows.movies.domain.entities.Movie
import com.ru.movieshows.movies.domain.repositories.MoviesRepository
import javax.inject.Inject

class InsertMovieSearchUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository,
) {

    suspend fun execute(movie: Movie, locale: String) {
        return moviesRepository.insertMovieSearch(movie, locale)
    }

}