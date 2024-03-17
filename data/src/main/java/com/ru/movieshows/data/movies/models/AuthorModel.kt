package com.ru.movieshows.data.movies.models

import com.google.gson.annotations.SerializedName

data class AuthorModel(
    @SerializedName("avatar_path")
    val avatarPath: String?,
    val rating: Double?,
)