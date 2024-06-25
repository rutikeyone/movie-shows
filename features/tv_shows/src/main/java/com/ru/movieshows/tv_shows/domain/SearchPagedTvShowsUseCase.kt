package com.ru.movieshows.tv_shows.domain

import androidx.paging.PagingData
import com.ru.movieshows.tv_shows.domain.entities.TvShow
import com.ru.movieshows.tv_shows.domain.repositories.TvShowsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchPagedTvShowsUseCase @Inject constructor(
    private val tvShowsRepository: TvShowsRepository,
) {

    suspend fun execute(
        language: String = "en_US",
        query: String? = null,
    ): Flow<PagingData<TvShow>> {
        return tvShowsRepository.searchPagedMovies(
            language = language,
            query = query,
        )
    }

}