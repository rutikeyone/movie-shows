package com.ru.movieshows.data.genres

import com.ru.movieshows.data.GenresDataRepository
import com.ru.movieshows.data.IODispatcher
import com.ru.movieshows.data.genres.models.GenreModel
import com.ru.movieshows.data.genres.sources.GenresSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GenresDataRepositoryImpl @Inject constructor(
    private val genresSource: GenresSource,
    @IODispatcher private val dispatcher: CoroutineDispatcher,
) : GenresDataRepository {

    override suspend fun getGenres(language: String): List<GenreModel> {
        return withContext(dispatcher) {
            genresSource.fetchGenres(language)
        }
    }

}