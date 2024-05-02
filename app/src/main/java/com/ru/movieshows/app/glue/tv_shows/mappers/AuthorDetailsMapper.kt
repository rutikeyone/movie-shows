package com.ru.movieshows.app.glue.tv_shows.mappers

import com.ru.movieshows.app.formatters.ImageUrlFormatter
import com.ru.movieshows.data.movies.models.AuthorModel
import com.ru.movieshows.tv_shows.domain.entities.AuthorDetails
import javax.inject.Inject

class AuthorDetailsMapper @Inject constructor(
    private val imageUrlFormatter: ImageUrlFormatter,
) {

    fun toAuthorDetails(model: AuthorModel): AuthorDetails {
        return AuthorDetails(
            avatarPath = imageUrlFormatter.toImageUrl(model.avatarPath),
            rating = model.rating,
        )
    }

}