package com.ru.movieshows.data.movies.models

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.ru.movieshows.data.movies.converters.ImageJsonDeserializer

data class AuthorModel(
    @SerializedName("avatar_path")
    @JsonAdapter(value = ImageJsonDeserializer::class)
    val avatar: String?,
    val rating: Double?,
)