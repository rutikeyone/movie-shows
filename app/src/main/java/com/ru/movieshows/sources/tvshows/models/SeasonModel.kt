package com.ru.movieshows.sources.tvshows.models

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.ru.movieshows.sources.tvshows.converters.DateConverter
import com.ru.movieshows.sources.tvshows.converters.ImageConverter
import com.ru.movieshows.sources.tvshows.entities.SeasonEntity
import java.util.Date

data class SeasonModel(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("episode_count")
    val episodeCountValue: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("overview")
    val overview: String?,
    @SerializedName("season_number")
    val seasonNumber: Int?,
    @SerializedName("vote_average")
    val rating: Double?,
    @SerializedName("air_date")
    @JsonAdapter(value = DateConverter::class)
    val airDate: Date?,
    @SerializedName("poster_path")
    @JsonAdapter(value = ImageConverter::class)
    val poster: String?,
    @SerializedName("episodes")
    val episodes: ArrayList<EpisodeModel>?,
) {
    fun toEntity(imageUrl: String): SeasonEntity {
        val posterUrl = if(!poster.isNullOrEmpty()) imageUrl + poster else null
        val episodes = this.episodes?.let { it ->
            ArrayList(it.map { it.toEntity(imageUrl) })
        }

        return SeasonEntity(
            id,
            episodeCountValue,
            name,
            overview,
            seasonNumber,
            rating,
            airDate,
            posterUrl,
            episodes,
        )
    }
}
