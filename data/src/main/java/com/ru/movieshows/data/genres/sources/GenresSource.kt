package com.ru.movieshows.data.genres.sources

import com.ru.movieshows.data.genres.models.GenreModel

interface GenresSource {

    suspend fun fetchGenres(
        language: String,
    ): List<GenreModel>

}