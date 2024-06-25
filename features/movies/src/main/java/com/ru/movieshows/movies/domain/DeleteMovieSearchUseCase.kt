package com.ru.movieshows.movies.domain

import com.ru.movieshows.movies.domain.repositories.MoviesRepository
import javax.inject.Inject

class DeleteMovieSearchUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository,
) {

    suspend fun execute(id: Long) {
        return moviesRepository.deleteMovieSearch(id)
    }

}