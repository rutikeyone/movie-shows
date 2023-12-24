package com.ru.movieshows.data.repository

import com.ru.movieshows.domain.entity.GenreEntity
import com.ru.movieshows.domain.repository.Repository

interface GenresRepository : Repository {
    suspend fun getGenres(language: String = "en_US"): Result<ArrayList<GenreEntity>>
}