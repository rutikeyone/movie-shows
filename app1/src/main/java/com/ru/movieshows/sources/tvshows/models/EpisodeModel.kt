package com.ru.movieshows.sources.tvshows.models

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.ru.movieshows.sources.tvshows.converters.DateConverter
import com.ru.movieshows.sources.tvshows.entities.EpisodeEntity
import java.util.Date

data class EpisodeModel(
    val id: String?,
    @SerializedName("air_date")
    @JsonAdapter(value = DateConverter::class)
    val airDate: Date?,
    @SerializedName("season_number")
    val seasonNumber: Int?,
    @SerializedName("episode_number")
    val episodeNumber: Int?,
    val name: String?,
    @SerializedName("show_id")
    val showId: String?,
    @SerializedName("still_path")
    val stillPath: String?,
    @SerializedName("vote_average")
    val rating: Double?,
    @SerializedName("overview")
    val overview: String?,
    val crew: ArrayList<CrewModel>?
) {
    fun toEntity(imageUrl: String): EpisodeEntity {
        val stillPath = if(this.stillPath != null) imageUrl + this.stillPath else null

        return EpisodeEntity(
            id,
            airDate,
            seasonNumber,
            episodeNumber,
            name,
            showId,
            stillPath,
            rating,
            overview,
            crew?.let { crew ->
                ArrayList(crew.map { it.toEntity(imageUrl) })
            }
        )
    }
}