package com.ru.movieshows.tv_shows.domain.entities

data class TvShowSearch(
    val id: Long,
    val tvShowId: String?,
    val name: String?,
    val timeInMilSeconds: Long,
)