package com.ru.movieshows.app.glue.tv_shows.mappers

import com.ru.movieshows.data.movies.models.ReviewModel
import com.ru.movieshows.tv_shows.domain.entities.Review
import javax.inject.Inject

class ReviewMapper @Inject constructor(
    private val authorDetailsMapper: AuthorDetailsMapper,
) {

    fun toReview(model: ReviewModel): Review {
        return Review(
            id = model.id,
            author = model.author,
            content = model.content,
            authorDetails = model.authorDetails?.let { authorDetailsMapper.toAuthorDetails(it) },
        );
    }

}