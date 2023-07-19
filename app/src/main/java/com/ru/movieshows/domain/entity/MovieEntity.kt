package com.ru.movieshows.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieEntity(
     val id: Int?,
     val rating: Double?,
     val title: String?,
     val backDrop: String?,
     val poster: String?,
     val overview: String?
): Parcelable
