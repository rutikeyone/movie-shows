package com.ru.movieshows.movies.domain.entities

data class Movie(
    val id: Int?,
    val rating: Double?,
    val title: String?,
    val backDrop: String?,
    val posterPath: String?,
    val overview: String?
)