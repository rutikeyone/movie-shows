package com.ru.movieshows.tv_shows.domain

import com.ru.movieshows.tv_shows.domain.repositories.TvShowsRepository
import javax.inject.Inject

class GetImagesBySeasonNumberUseCase @Inject constructor(
    private val tvShowsRepository: TvShowsRepository,
) {

    suspend fun execute(
        language: String = "en_US",
        seriesId: String,
        seasonNumber: String,
    ): List<String>? {
        return tvShowsRepository.getImagesBySeasonNumber(
            language = language,
            seriesId = seriesId,
            seasonNumber = seasonNumber,
        )
    }

}