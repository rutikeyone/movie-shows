package com.ru.movieshows.sources.tv_shows.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data  class SeasonEntity(
    val id: Int?,
    val episodeCountValue: Int?,
    val name: String?,
    val overview: String?,
    val seasonNumber: Int?,
    val rating: Double?,
    val airDate: Date?,
    val poster: String?,
    val episodes: ArrayList<EpisodeEntity>?,
) : Parcelable {
    val episodeCount get() = episodes?.size ?: episodeCountValue
}