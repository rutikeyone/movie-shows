package com.ru.movieshows.sources.movies.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VideoEntity(
    val id: String?,
    val key: String?,
    val name: String?,
    val site: String?,
    val type: String?,
    val image: String?,
) : Parcelable