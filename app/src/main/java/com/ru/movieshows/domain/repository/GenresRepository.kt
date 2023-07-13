package com.ru.movieshows.domain.repository

import com.ru.movieshows.domain.entity.GenreEntity

interface GenresRepository {
    suspend fun getGenres(language: String = "en_US"): Result<ArrayList<GenreEntity>>
}