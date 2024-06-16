package com.ru.movieshows.tv_shows.domain

import com.ru.movieshows.tv_shows.domain.entities.Episode
import com.ru.movieshows.tv_shows.domain.repositories.TvShowsRepository
import javax.inject.Inject

class GetEpisodesBySeasonUseCase @Inject constructor(
    private val tvShowsRepository: TvShowsRepository,
) {

    suspend fun execute(
        language: String = "en_US",
        seriesId: String,
        seasonNumber: String,
    ): List<Episode> {
        val season = tvShowsRepository.getSeason(
            language = language,
            seriesId = seriesId,
            seasonNumber = seasonNumber,
        )

        return season.episodes ?: listOf()

    }

}