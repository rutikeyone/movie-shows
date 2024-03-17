package com.ru.movieshows.data.tvshows.models

import com.google.gson.annotations.SerializedName

data class TvShowModel(
    val id: String?,
    @SerializedName("vote_average")
    val rating: Double?,
    val name: String?,
    @SerializedName("backdrop_path")
    val backDropPath: String?,
    @SerializedName("poster_path")
    val posterPath: String?,
    val overview: String?
)