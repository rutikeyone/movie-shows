package com.ru.movieshows.data.movies.models

import com.google.gson.annotations.SerializedName

data class MoviesPaginationModel(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val results: List<MovieModel>,
    @SerializedName("total_pages") val totalPages: Int,
)