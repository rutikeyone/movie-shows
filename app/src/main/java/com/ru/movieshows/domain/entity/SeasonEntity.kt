package com.ru.movieshows.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data  class SeasonEntity(
    val id: Int?,
    val episodeCount: Int?,
    val name: String?,
    val overview: String?,
    val seasonNumber: Int?,
    val rating: Double?,
    val airDate: Date?,
    val poster: String?
) : Parcelable