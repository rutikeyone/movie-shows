package com.ru.movieshows.data.movies.models

import com.google.gson.annotations.SerializedName

data class MovieModel(
    val id: Int?,
    @SerializedName("vote_average")
    val rating: Double?,
    val title: String?,
    @SerializedName("backdrop_path")
    val backDropPath: String?,
    @SerializedName("poster_path")
    val posterPath: String?,
    val overview: String?,
)