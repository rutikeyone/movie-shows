package com.ru.movieshows.sources.genres.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.ru.movieshows.sources.genres.entities.GenreEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class GenreModel(
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val name: String?,
) : Parcelable {

    fun toEntity(): GenreEntity = GenreEntity(id, name)

}
