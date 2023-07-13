package com.ru.movieshows.data.response

import com.google.gson.annotations.SerializedName
import com.ru.movieshows.data.model.GenreModel

data class GetGenresResponse(
    @SerializedName("genres")
    val genres: ArrayList<GenreModel>
)