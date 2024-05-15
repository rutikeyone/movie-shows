package com.ru.movieshows.movies.domain

import androidx.paging.PagingData
import com.ru.movieshows.movies.domain.entities.Movie
import com.ru.movieshows.movies.domain.repositories.MoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPagedTopRatedMoviesUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository,
) {

    fun execute(
        language: String = "en_US",
    ): Flow<PagingData<Movie>> {
        return moviesRepository.getPagedTopRatedMovies(language)
    }

}