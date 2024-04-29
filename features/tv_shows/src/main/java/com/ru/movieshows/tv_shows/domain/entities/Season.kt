package com.ru.movieshows.tv_shows.domain.entities

import java.util.Date

data class Season(
    val id: Int?,
    val episodeCountValue: Int?,
    val name: String?,
    val overview: String?,
    val seasonNumber: Int?,
    val rating: Double?,
    val airDate: Date?,
    val posterPath: String?,
    val episodes: List<Episode>?,
    val episodeCount: Int?,
)