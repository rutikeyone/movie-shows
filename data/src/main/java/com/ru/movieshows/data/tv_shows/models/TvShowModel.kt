package com.ru.movieshows.data.tv_shows.models

import com.google.gson.annotations.SerializedName

data class TvShowModel(
    @SerializedName("id") val id: String?,
    @SerializedName("vote_average") val rating: Double?,
    @SerializedName("name") val name: String?,
    @SerializedName("backdrop_path") val backDropPath: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("overview") val overview: String?
)