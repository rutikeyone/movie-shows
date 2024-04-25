package com.ru.movieshows.app.glue.movies.mappers

import com.ru.movieshows.data.movies.models.ReviewsPaginationModel
import com.ru.movieshows.movies.domain.entities.ReviewsPagination
import javax.inject.Inject

class ReviewsPaginationMapper @Inject constructor(
    private val reviewMapper: ReviewMapper,
) {

    fun toReviewsPagination(model: ReviewsPaginationModel): ReviewsPagination {
        return ReviewsPagination(
            page = model.page,
            results = model.results.map { reviewMapper.toReview(it) },
            totalPages = model.totalPages,
        );
    }

}