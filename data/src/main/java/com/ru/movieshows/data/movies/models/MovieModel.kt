package com.ru.movieshows.data.movies.models

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.ru.movieshows.data.movies.converters.ImageJsonDeserializer

data class MovieModel(
    val id: Int?,
    @SerializedName("vote_average")
    val rating: Double?,
    val title: String?,
    @SerializedName("backdrop_path")
    @JsonAdapter(value = ImageJsonDeserializer::class)
    val backDrop: String?,
    @SerializedName("poster_path")
    @JsonAdapter(value = ImageJsonDeserializer::class)
    val poster: String?,
    val overview: String?
)