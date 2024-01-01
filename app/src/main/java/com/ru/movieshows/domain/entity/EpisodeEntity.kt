package com.ru.movieshows.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class EpisodeEntity(
    val id: String?,
    val airDate: Date?,
    val seasonNumber: Int?,
    val episodeNumber: Int?,
    val name: String?,
    val showId: String?,
    val stillPath: String?,
    val rating: Double?,
    val overview: String?,
    val crew: ArrayList<CrewEntity>?
) : Parcelable