package com.ru.movieshows.tv_shows.domain

import com.ru.movieshows.tv_shows.domain.entities.Review
import com.ru.movieshows.tv_shows.domain.entities.ReviewPagination
import com.ru.movieshows.tv_shows.domain.repositories.TvShowsRepository
import javax.inject.Inject

class GetTvShowReviewsUseCase @Inject constructor(
    private val tvShowsRepository: TvShowsRepository,
) {

    suspend fun execute(
        language: String = "en_US",
        seriesId: String,
        page: Int = 1,
    ): ReviewPagination {
        return tvShowsRepository.getTvShowReviews(
            language = language,
            seriesId = seriesId,
            page = page,
        )
    }

}