package com.ru.movieshows.data.movies.models

import com.google.gson.annotations.SerializedName

@JvmInline
value class VideosModel(
    @SerializedName("results") val results: List<VideoModel>,
)