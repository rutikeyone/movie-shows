package com.ru.movieshows.data.tv_shows.models

import com.google.gson.annotations.SerializedName

data class SeasonModel(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("episode_count") val episodeCount: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("overview") val overview: String?,
    @SerializedName("season_number") val seasonNumber: Int?,
    @SerializedName("vote_average") val rating: Double?,
    @SerializedName("air_date") val airDate: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("episodes") val episodes: List<EpisodeModel>?,
)