package com.ru.movieshows.domain.entity

import com.ru.movieshows.data.model.GenreModel

data class TvDetailsEntity(
    val id: Int?,
    val genres: ArrayList<GenreModel>,
    val firstAirDate: String?,
    val overview: String?,
    val backDrop: String?,
    val poster: String?,
    val rating: Double?,
    val name: String?,
    val numberOfEpisodes: Int?,
    val numberOfSeasons: Int?,
)