package com.ru.movieshows.tv_shows.domain

import com.ru.movieshows.tv_shows.domain.repositories.TvShowsRepository
import javax.inject.Inject

class GetImagesByEpisodeIdUseCase @Inject constructor(
    private val tvShowsRepository: TvShowsRepository,
) {

    suspend fun execute(
        seriesId: String,
        seasonNumber: String,
        episodeNumber: Int,
    ): List<String>? {

        return tvShowsRepository.getImagesByEpisodeId(
            seriesId = seriesId,
            seasonNumber = seasonNumber,
            episodeNumber = episodeNumber,
        )
    }

}