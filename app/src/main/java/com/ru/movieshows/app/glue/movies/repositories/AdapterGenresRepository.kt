package com.ru.movieshows.app.glue.movies.repositories

import com.ru.movieshows.data.GenresDataRepository
import com.ru.movieshows.movies.domain.entities.Genre
import com.ru.movieshows.movies.domain.repositories.GenresRepository
import javax.inject.Inject

class AdapterGenresRepository @Inject constructor(
    private val genresDataRepository: GenresDataRepository,
) : GenresRepository {
    override suspend fun getGenres(language: String): List<Genre> {
        val result = genresDataRepository.getGenres(language)
        return result.map {
            Genre(
                id = it.id,
                name = it.name,
            )
        }
    }

}