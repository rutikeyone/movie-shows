package com.ru.movieshows.tv_shows.domain.entities

data class TvShowPagination(
    val page: Int,
    val results: List<TvShow>,
    val totalPages: Int,
)