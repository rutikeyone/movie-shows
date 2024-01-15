package com.ru.movieshows.sources.genres.models

import com.google.gson.annotations.SerializedName

data class GenresResponseModel(
    @SerializedName("genres")
    val genres: ArrayList<GenreModel>,
)