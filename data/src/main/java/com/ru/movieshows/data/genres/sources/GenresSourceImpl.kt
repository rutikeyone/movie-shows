package com.ru.movieshows.data.genres.sources

import com.google.gson.Gson
import com.ru.movieshows.data.BaseRetrofitSource
import com.ru.movieshows.data.genres.api.GenresApi
import com.ru.movieshows.data.genres.models.GenreModel
import javax.inject.Inject

class GenresSourceImpl @Inject constructor(
    private val genresApi: GenresApi,
    private val gson: Gson,
) : GenresSource, BaseRetrofitSource(gson) {

    override suspend fun fetchGenres(language: String): List<GenreModel> {
        return genresApi
            .getGenres(language)
            .awaitResult { it.genres }
    }

}