package com.ru.movieshows.data.model

import com.google.gson.annotations.SerializedName
import com.ru.movieshows.domain.entity.GenreEntity

data class GenreModel(
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val name: String?,
) {
    fun toEntity(): GenreEntity = GenreEntity(id, name)
}
