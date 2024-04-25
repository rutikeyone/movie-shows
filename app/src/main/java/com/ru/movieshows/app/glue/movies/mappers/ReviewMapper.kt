package com.ru.movieshows.app.glue.movies.mappers

import com.ru.movieshows.data.movies.models.ReviewModel
import com.ru.movieshows.movies.domain.entities.Review
import javax.inject.Inject

class ReviewMapper @Inject constructor(
    private val authorMapper: AuthorMapper,
) {

    fun toReview(model: ReviewModel): Review {
        return Review(
            id = model.id,
            author = model.author,
            content = model.content,
            authorDetails = authorMapper.toAuthor(model.authorDetails)
        );
    }

}