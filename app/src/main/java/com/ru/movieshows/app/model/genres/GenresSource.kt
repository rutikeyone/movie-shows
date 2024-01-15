package com.ru.movieshows.app.model.genres

import com.ru.movieshows.sources.genres.entities.GenreEntity

interface GenresSource {

    suspend fun getGenres(language: String = "en_US"): ArrayList<GenreEntity>

}