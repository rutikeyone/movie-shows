package com.ru.movieshows.data.genres

import com.ru.movieshows.data.GenresDataRepository
import com.ru.movieshows.data.genres.models.GenreModel
import com.ru.movieshows.data.genres.sources.GenresSource
import javax.inject.Inject

class GenresDataRepositoryImpl @Inject constructor(
    private val genresSource: GenresSource,
) : GenresDataRepository {

    override suspend fun getGenres(language: String): List<GenreModel> {
        return genresSource.fetchGenres(language)
    }

}