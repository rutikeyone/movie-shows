package com.ru.movieshows.tv_shows.domain

import androidx.paging.PagingData
import com.ru.movieshows.tv_shows.domain.entities.Review
import com.ru.movieshows.tv_shows.domain.repositories.TvShowsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPagedTvShowReviewsUseCase @Inject constructor(
    private val tvShowsRepository: TvShowsRepository,
) {

    fun execute(
        language: String = "en_US",
        id: String,
    ): Flow<PagingData<Review>> {
        return tvShowsRepository.getPagedTvShowReviews(
            language = language,
            id = id,
        )
    }

}