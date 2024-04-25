package com.ru.movieshows.movies.domain.entities

data class MoviesPagination(
    val page: Int,
    val results: List<Movie>,
    val totalPages: Int,
)