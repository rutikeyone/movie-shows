package com.ru.movieshows.movies.domain

import androidx.paging.PagingData
import com.ru.movieshows.movies.domain.entities.Movie
import com.ru.movieshows.movies.domain.repositories.MoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchPagedMoviesUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository,
) {

    suspend fun execute(
        language: String = "en_US",
        query: String? = null,
    ): Flow<PagingData<Movie>> {
        return moviesRepository.searchPagedMovies(
            language = language,
            query = query,
        )
    }

}