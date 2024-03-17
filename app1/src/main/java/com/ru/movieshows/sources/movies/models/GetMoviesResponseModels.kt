package com.ru.movieshows.sources.movies.models

import com.google.gson.annotations.SerializedName

data class GetMoviesResponseModels(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: ArrayList<MovieModel>,
    @SerializedName("total_pages")
    val totalPages: Int,
)