package com.ru.movieshows.app.model.genres

import arrow.core.Either
import com.ru.movieshows.app.model.AppFailure
import com.ru.movieshows.sources.genres.entities.GenreEntity

interface GenresRepository {

    suspend fun getGenres(language: String = "en_US"): Either<AppFailure, ArrayList<GenreEntity>>

}