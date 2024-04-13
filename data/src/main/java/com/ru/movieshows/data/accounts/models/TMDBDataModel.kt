package com.ru.movieshows.data.accounts.models

import com.google.gson.annotations.SerializedName

data class TMDBDataModel(
    @SerializedName("avatar_path") val avatarPath: String?,
)