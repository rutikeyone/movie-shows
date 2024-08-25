package com.ru.movieshows.data.tv_shows.models

import com.google.gson.annotations.SerializedName

@JvmInline
value class ImagesModel(
    @SerializedName("stills") val stills: List<ImageModel>?
)