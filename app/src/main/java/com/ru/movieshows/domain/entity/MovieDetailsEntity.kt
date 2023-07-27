package com.ru.movieshows.domain.entity

import com.ru.movieshows.data.model.GenreModel
import java.util.Date

data class MovieDetailsEntity(
    val id: Int?,
    val genres: ArrayList<GenreEntity>,
    val releaseDate: Date?,
    val overview: String?,
    val backDrop: String?,
    val poster: String?,
    val rating: Double?,
    val title: String?,
    val runtime: String?,
)