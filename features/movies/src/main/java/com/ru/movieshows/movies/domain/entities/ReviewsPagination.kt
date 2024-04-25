package com.ru.movieshows.movies.domain.entities

data class ReviewsPagination(
    val page: Int,
    val results: List<Review>,
    val totalPages: Int,
)