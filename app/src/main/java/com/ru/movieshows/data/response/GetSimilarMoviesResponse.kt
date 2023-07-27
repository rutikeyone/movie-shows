package com.ru.movieshows.data.response

import com.google.gson.annotations.SerializedName
import com.ru.movieshows.data.model.MovieModel
import com.ru.movieshows.data.model.TvShowsModel
import com.ru.movieshows.domain.entity.MovieEntity

data class GetSimilarMoviesResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: ArrayList<MovieModel>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int,
)