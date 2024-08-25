package com.ru.movieshows.data.tv_shows.models

import com.google.gson.annotations.SerializedName

data class ImageModel(
    @SerializedName("file_path") val filePath: String?,
)