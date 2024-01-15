package com.ru.movieshows.sources.tv_shows.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TvShowsEntity(
    val id: String?,
    val rating: Double?,
    val name: String?,
    val backDrop: String?,
    val poster: String?,
    val overview: String?
): Parcelable
