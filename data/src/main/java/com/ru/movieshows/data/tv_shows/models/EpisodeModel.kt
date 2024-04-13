package com.ru.movieshows.data.tv_shows.models

import com.google.gson.annotations.SerializedName

data class EpisodeModel(
    @SerializedName("id") val id: String?,
    @SerializedName("air_date") val airDate: String?,
    @SerializedName("season_number") val seasonNumber: Int?,
    @SerializedName("episode_number") val episodeNumber: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("show_id") val showId: String?,
    @SerializedName("still_path") val still: String?,
    @SerializedName("vote_average") val rating: Double?,
    @SerializedName("overview") val overview: String?,
    @SerializedName("crew") val crew: List<CrewModel>?,
)