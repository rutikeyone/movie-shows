package com.ru.movieshows.data.tvshows.models

import com.google.gson.annotations.SerializedName

data class EpisodeModel(
    val id: String?,
    @SerializedName("air_date")
    val airDate: String?,
    @SerializedName("season_number")
    val seasonNumber: Int?,
    @SerializedName("episode_number")
    val episodeNumber: Int?,
    val name: String?,
    @SerializedName("show_id")
    val showId: String?,
    @SerializedName("still_path")
    val still: String?,
    @SerializedName("vote_average")
    val rating: Double?,
    val overview: String?,
    val crew: List<CrewModel>?
)