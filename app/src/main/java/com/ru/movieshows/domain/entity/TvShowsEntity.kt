package com.ru.movieshows.domain.entity

import com.google.gson.annotations.SerializedName

data class TvShowsEntity(
    val id: String?,
    val rating: Double?,
    val name: String?,
    val backDrop: String?,
    val poster: String?,
    val overview: String?
)
