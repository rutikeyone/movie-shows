package com.ru.movieshows.data.movies.models

import com.google.gson.annotations.SerializedName

data class MovieModel(
    @SerializedName("id") val id: Int?,
    @SerializedName("vote_average") val rating: Double?,
    @SerializedName("title") val title: String?,
    @SerializedName("backdrop_path") val backDropPath: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("overview") val overview: String?,
)