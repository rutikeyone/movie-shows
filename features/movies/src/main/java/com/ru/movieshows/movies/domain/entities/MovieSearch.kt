package com.ru.movieshows.movies.domain.entities

data class MovieSearch(
    val id: Long,
    val movieId: Int?,
    val name: String?,
    val timeInMilSeconds: Long,
)