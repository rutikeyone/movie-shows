package com.ru.movieshows.tv_shows.domain.entities

import java.util.Date

data class Episode(
    val id: String?,
    val airDate: Date?,
    val seasonNumber: Int?,
    val episodeNumber: Int?,
    val name: String?,
    val showId: String?,
    val stillPath: String?,
    val rating: Double?,
    val overview: String?,
    val crew: List<Crew>?,
)