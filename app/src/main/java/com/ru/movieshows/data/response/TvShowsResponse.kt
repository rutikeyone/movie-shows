package com.ru.movieshows.data.response

import com.google.gson.annotations.SerializedName
import com.ru.movieshows.data.model.TvShowsModel

data class TvShowsResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val result: ArrayList<TvShowsModel>
)