package com.ru.movieshows.tv_shows.domain

import com.ru.movieshows.tv_shows.domain.entities.Season
import com.ru.movieshows.tv_shows.domain.repositories.TvShowsRepository
import javax.inject.Inject

class GetSeasonUseCase @Inject constructor(
    private val tvShowRepository: TvShowsRepository,
) {

    suspend fun execute(
        language: String = "en_US",
        seriesId: String,
        seasonNumber: String,
    ): Season {
        return tvShowRepository.getSeason(
            language = language,
            seriesId = seriesId,
            seasonNumber = seasonNumber,
        )
    }

}