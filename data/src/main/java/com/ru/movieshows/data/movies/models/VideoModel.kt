package com.ru.movieshows.data.movies.models

import com.google.gson.annotations.SerializedName

data class VideoModel(
    @SerializedName("id") val id: String?,
    @SerializedName("key") val key: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("site") val site: String?,
    @SerializedName("type") val type: String?,
)