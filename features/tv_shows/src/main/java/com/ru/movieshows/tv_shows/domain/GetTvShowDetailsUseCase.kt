package com.ru.movieshows.tv_shows.domain

import com.ru.movieshows.tv_shows.domain.entities.Review
import com.ru.movieshows.tv_shows.domain.entities.TvShowDetails
import com.ru.movieshows.tv_shows.domain.repositories.TvShowsRepository
import javax.inject.Inject

class GetTvShowDetailsUseCase @Inject constructor(
    private val tvShowsRepository: TvShowsRepository,
) {

    suspend fun execute(
        language: String = "en_US",
        id: String,
    ): TvShowDetails {
        return tvShowsRepository.getTvShowDetails(
            language, id
        )
    }

}