package com.ru.movieshows.tv_shows.domain.entities

data class Review(
    val id: String?,
    val author: String?,
    val content: String?,
    val authorDetails: AuthorDetails?,
)