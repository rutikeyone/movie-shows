package com.ru.movieshows.data.tvshows.models

import com.google.gson.annotations.SerializedName

data class TvShowPaginationModel(
    val page: Int,
    val result: List<TvShowModel>,
    @SerializedName("total_pages")
    val totalPages: Int,
)