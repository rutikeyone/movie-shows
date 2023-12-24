package com.ru.movieshows.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class EpisodeEntity(
    val id: String?,
    val airDate: Date?,
    val seasonNumber: Int?,
    val name: String?,
    val showId: String?,
    val stillPath: String?,
    val rating: Double?,
) : Parcelable