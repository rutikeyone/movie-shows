package com.ru.movieshows.data

import com.ru.movieshows.data.genres.models.GenreModel

interface GenresDataRepository {

    suspend fun fetchGenres(language: String = "en_US"): List<GenreModel>

}