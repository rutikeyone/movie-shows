package com.ru.movieshows.app.glue.tv_shows.mappers

import com.ru.movieshows.data.genres.models.GenreModel
import com.ru.movieshows.tv_shows.domain.entities.Genre
import javax.inject.Inject

class TvShowGenreMapper @Inject constructor() {

    fun toGenre(model: GenreModel): Genre {
        return Genre(
            id = model.id,
            name = model.name,
        );
    }

}