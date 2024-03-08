package com.ru.movieshows.data.genres.sources

import com.ru.movieshows.data.genres.api.GenresApi
import com.ru.movieshows.data.genres.models.GenreModel
import javax.inject.Inject

class GenresSourceImpl @Inject constructor(
    private val genresApi: GenresApi,
) : GenresSource {

    override suspend fun fetchGenres(language: String): List<GenreModel> {
        val response = genresApi.getGenres(language)
        return response.body()?.genres ?: throw NullPointerException()
    }

}