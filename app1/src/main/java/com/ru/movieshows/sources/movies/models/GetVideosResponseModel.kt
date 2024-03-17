package com.ru.movieshows.sources.movies.models

import com.google.gson.annotations.SerializedName

data class GetVideosResponseModel(
    @SerializedName("id")
    val id: String?,
    @SerializedName("results")
    val results: ArrayList<VideoModel>
)