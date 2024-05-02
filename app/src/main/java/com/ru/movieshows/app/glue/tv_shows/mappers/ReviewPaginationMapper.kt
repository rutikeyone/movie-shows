package com.ru.movieshows.app.glue.tv_shows.mappers

import com.ru.movieshows.data.movies.models.ReviewsPaginationModel
import com.ru.movieshows.tv_shows.domain.entities.ReviewPagination
import javax.inject.Inject

class ReviewPaginationMapper @Inject constructor(
    private val reviewMapper: ReviewMapper,
) {

    fun toReviewPagination(model: ReviewsPaginationModel): ReviewPagination {
        return ReviewPagination(
            page = model.page,
            results = model.results.map { reviewMapper.toReview(it) },
            totalPages = model.totalPages,
        );
    }

}