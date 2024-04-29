package com.ru.movieshows.tv_shows.domain.entities

import java.util.Date

data class TvShowDetails(
    val id: Int?,
    val firstAirDate: Date?,
    val overview: String?,
    val backDropPath: String?,
    val posterPath: String?,
    val rating: Double?,
    val name: String?,
    val numberOfEpisodes: Int?,
    val numberOfSeasons: Int?
)