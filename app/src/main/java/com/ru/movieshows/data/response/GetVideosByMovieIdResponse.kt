package com.ru.movieshows.data.response

import com.google.gson.annotations.SerializedName
import com.ru.movieshows.data.model.VideoModel

data class GetVideosByMovieIdResponse(
    @SerializedName("id")
    val id: String?,
    @SerializedName("results")
    val results: ArrayList<VideoModel>
)