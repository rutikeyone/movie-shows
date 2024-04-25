package com.ru.movieshows.app.glue.movies.mappers

import com.ru.movieshows.data.genres.models.GenreModel
import com.ru.movieshows.movies.domain.entities.Genre
import javax.inject.Inject

class GenreMapper @Inject constructor() {

    fun toGenre(model: GenreModel): Genre {
        return Genre(
            id = model.id,
            name = model.name,
        )
    }

}