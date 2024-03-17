package com.ru.movieshows.sources.tvshows.models

import com.google.gson.annotations.SerializedName

data class TvShowsResponseModel(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val result: ArrayList<TvShowsModel>,
    @SerializedName("total_pages")
    val totalPages: Int
)