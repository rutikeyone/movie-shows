package com.ru.movieshows.data.accounts.models

import com.google.gson.annotations.SerializedName

data class AvatarDataModel(
    @SerializedName("tmdb") val tmdb: TMDBDataModel,
)