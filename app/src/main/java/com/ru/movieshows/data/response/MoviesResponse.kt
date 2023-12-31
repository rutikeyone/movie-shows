package com.ru.movieshows.data.response

import com.google.gson.annotations.SerializedName
import com.ru.movieshows.data.model.MovieModel

data class MoviesResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: ArrayList<MovieModel>,
    @SerializedName("total_pages")
    val totalPages: Int,
)