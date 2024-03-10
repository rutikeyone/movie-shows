package com.ru.movieshows.data.genres.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GenreModel(
    val id: String?,
    val name: String?,
): Parcelable