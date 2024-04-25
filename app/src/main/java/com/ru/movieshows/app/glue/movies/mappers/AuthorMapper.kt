package com.ru.movieshows.app.glue.movies.mappers

import com.ru.movieshows.data.movies.models.AuthorModel
import com.ru.movieshows.movies.domain.entities.Author
import javax.inject.Inject

class AuthorMapper @Inject constructor() {

    fun toAuthor(model: AuthorModel?): Author? {
        if (model == null) {
            return null;
        }

        return Author(
            avatarPath = model.avatarPath,
            rating = model.rating,
        );
    }

}