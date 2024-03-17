package com.ru.movieshows.data.tvshows.models

import com.google.gson.annotations.SerializedName

data class SeasonModel(
    val id: Int? = null,
    @SerializedName("episode_count")
    val episodeCount: Int?,
    val name: String?,
    val overview: String?,
    @SerializedName("season_number")
    val seasonNumber: Int?,
    @SerializedName("vote_average")
    val rating: Double?,
    @SerializedName("air_date")
    val airDate: String?,
    @SerializedName("poster_path")
    val posterPath: String?,
    val episodes: List<EpisodeModel>?,
)