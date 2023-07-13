package com.ru.movieshows.domain.entity

import com.ru.movieshows.data.model.GenreModel

data class MovieDetailsEntity(
    val id: Int?,
    val genres: ArrayList<GenreModel>,
    val releaseDate: String?,
    val overview: String?,
    val backDrop: String?,
    val poster: String?,
    val rating: Double?,
    val title: String?,
)