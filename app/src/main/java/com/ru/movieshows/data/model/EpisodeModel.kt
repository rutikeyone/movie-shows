package com.ru.movieshows.data.model

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.ru.movieshows.BuildConfig
import com.ru.movieshows.data.utils.DateConverter
import com.ru.movieshows.domain.entity.EpisodeEntity
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
    fun toEntity(): EpisodeEntity {
        return EpisodeEntity(
            id,
            airDate,
            seasonNumber,
            episodeNumber,
            name,
            showId,
            if(this.stillPath != null) BuildConfig.TMDB_IMAGE_URL + this.stillPath else null,
            rating,
            overview,
            crew?.let { crew ->
                ArrayList(crew.map { it.toEntity() })
            }
        )
    }
}