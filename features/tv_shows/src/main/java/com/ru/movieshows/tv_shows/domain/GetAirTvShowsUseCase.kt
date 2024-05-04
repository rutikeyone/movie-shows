package com.ru.movieshows.tv_shows.domain

import com.ru.movieshows.tv_shows.domain.entities.TvShowPagination
import com.ru.movieshows.tv_shows.domain.repositories.TvShowsRepository
import javax.inject.Inject

class GetAirTvShowsUseCase @Inject constructor(
    private val tvShowsRepository: TvShowsRepository,
){

    suspend fun execute(
        language: String = "en_US",
        page: Int = 1,
    ): TvShowPagination {

        return tvShowsRepository.getOnTheAirTvShows(
            language = language,
            page = page,
        )
    }

}