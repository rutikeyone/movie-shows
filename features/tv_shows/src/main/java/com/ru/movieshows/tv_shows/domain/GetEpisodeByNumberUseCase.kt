package com.ru.movieshows.tv_shows.domain

import com.ru.movieshows.tv_shows.domain.entities.Episode
import com.ru.movieshows.tv_shows.domain.repositories.TvShowsRepository
import javax.inject.Inject

class GetEpisodeByNumberUseCase @Inject constructor(
    private val tvShowsRepository: TvShowsRepository,
) {

    suspend fun execute(
        language: String,
        seriesId: String,
        seasonNumber: String,
        episodeNumber: Int,
    ): Episode {
        return tvShowsRepository.getEpisodeByNumber(
            language = language,
            seriesId = seriesId,
            seasonNumber = seasonNumber,
            episodeNumber = episodeNumber,
        )
    }

}