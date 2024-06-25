package com.ru.movieshows.movies.domain

import com.ru.movieshows.movies.domain.entities.MovieSearch
import com.ru.movieshows.movies.domain.repositories.MoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllMoviesSearchUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository,
) {

    fun execute(locale: String): Flow<List<MovieSearch>> {
        return moviesRepository.getAllMoviesSearch(locale)
    }

}