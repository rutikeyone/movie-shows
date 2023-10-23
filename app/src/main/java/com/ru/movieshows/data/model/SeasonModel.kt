package com.ru.movieshows.data.model

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.ru.movieshows.data.utils.DateConverter
import com.ru.movieshows.data.utils.ImageConverter
import com.ru.movieshows.domain.entity.SeasonEntity
import java.util.Date

data class SeasonModel(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("episode_count")
    val episodeCount: Int?,
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
    val poster: String?
) {
    fun toEntity(): SeasonEntity = SeasonEntity(
        id,
        episodeCount,
        name,
        overview,
        seasonNumber,
        rating,
        airDate,
        poster,
    )
}
