package com.ru.movieshows.data.genres.sources

import com.google.gson.Gson
import com.ru.movieshows.data.BaseRetrofitSource
import com.ru.movieshows.data.genres.services.GenresService
import com.ru.movieshows.data.genres.models.GenreModel
import javax.inject.Inject

class GenresSourceImpl @Inject constructor(
    private val genresService: GenresService,
    private val gson: Gson,
) : GenresSource, BaseRetrofitSource(gson) {

    override suspend fun fetchGenres(language: String): List<GenreModel> {
        return genresService
            .getGenres(language)
            .awaitResult { it.genres }
    }

}