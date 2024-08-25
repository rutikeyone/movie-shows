package com.ru.movieshows.tv_shows.domain

import com.ru.movieshows.tv_shows.domain.entities.TvShowPagination
import com.ru.movieshows.tv_shows.domain.repositories.TvShowsRepository
import javax.inject.Inject

class GetSimilarTvShowsUseCase @Inject constructor(
    private val tvShowsRepository: TvShowsRepository,
) {

    suspend fun execute(
        language: String = "en_US",
        id: String,
        page: Int = 1,
    ): TvShowPagination {

        return tvShowsRepository.getSimilarTvShows(
            language = language,
            page = page,
            id = id,
        )

    }

}