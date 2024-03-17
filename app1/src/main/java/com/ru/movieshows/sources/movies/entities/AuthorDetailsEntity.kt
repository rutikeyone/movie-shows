package com.ru.movieshows.sources.movies.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AuthorDetailsEntity(
    val avatar: String?,
    val rating: Double?
): Parcelable