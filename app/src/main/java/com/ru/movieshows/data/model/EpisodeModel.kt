package com.ru.movieshows.data.model

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
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
    val name: String?,
    @SerializedName("show_id")
    val showId: String?,
) {
    fun toEntity(): EpisodeEntity = EpisodeEntity(id, airDate, seasonNumber, name, showId)
}