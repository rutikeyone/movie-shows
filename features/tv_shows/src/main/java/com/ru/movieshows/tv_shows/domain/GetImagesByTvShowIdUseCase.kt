package com.ru.movieshows.tv_shows.domain

import com.ru.movieshows.tv_shows.domain.repositories.TvShowsRepository
import javax.inject.Inject

class GetImagesByTvShowIdUseCase @Inject constructor(
    private val tvShowsRepository: TvShowsRepository,
) {

    suspend fun execute(id: String): List<String>? {
        return tvShowsRepository.getImagesByTvShowId(id)
    }

}