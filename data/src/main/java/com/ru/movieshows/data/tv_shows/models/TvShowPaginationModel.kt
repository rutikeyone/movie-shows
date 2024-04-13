package com.ru.movieshows.data.tv_shows.models

import com.google.gson.annotations.SerializedName

data class TvShowPaginationModel(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val result: List<TvShowModel>,
    @SerializedName("total_pages") val totalPages: Int,
)