package com.ru.movieshows.tv_shows.domain

import com.ru.movieshows.tv_shows.domain.entities.Video
import com.ru.movieshows.tv_shows.domain.repositories.TvShowsRepository
import javax.inject.Inject

class GetVideosByIdUseCase @Inject constructor(
    private val tvShowsRepository: TvShowsRepository,
) {

    suspend fun execute(
        language: String = "en_US",
        seriesId: String,
    ): List<Video> {
        return tvShowsRepository.getVideosById(
            language, seriesId
        )
    }

}