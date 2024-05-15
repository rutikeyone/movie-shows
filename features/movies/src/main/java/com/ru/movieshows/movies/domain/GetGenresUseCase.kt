package com.ru.movieshows.movies.domain

import com.ru.movieshows.movies.domain.entities.Genre
import com.ru.movieshows.movies.domain.repositories.GenresRepository
import javax.inject.Inject

class GetGenresUseCase @Inject constructor(
    private val genresRepository: GenresRepository,
) {

    suspend fun execute(
        language: String = "en_US",
    ): List<Genre> {
        return genresRepository.getGenres(language)
    }

}