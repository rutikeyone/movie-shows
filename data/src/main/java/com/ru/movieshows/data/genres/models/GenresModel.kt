package com.ru.movieshows.data.genres.models

import com.google.gson.annotations.SerializedName

@JvmInline
value class GenresModel(
    @SerializedName("genres") val genres: List<GenreModel>,
)