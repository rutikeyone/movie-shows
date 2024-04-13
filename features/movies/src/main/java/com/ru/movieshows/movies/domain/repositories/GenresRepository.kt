package com.ru.movieshows.movies.domain.repositories

import com.ru.movieshows.movies.domain.entities.Genre

interface GenresRepository {

    suspend fun getGenres(language: String): List<Genre>


}