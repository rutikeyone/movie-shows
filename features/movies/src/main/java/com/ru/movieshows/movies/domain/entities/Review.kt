package com.ru.movieshows.movies.domain.entities

data class Review(
    val id: String?,
    val author: String?,
    val content: String?,
    val authorDetails: Author?,
)