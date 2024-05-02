package com.ru.movieshows.tv_shows.domain.entities

data class ReviewPagination(
    val page: Int,
    val results: List<Review>,
    val totalPages: Int,
)