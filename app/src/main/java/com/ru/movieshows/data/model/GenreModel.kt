package com.ru.movieshows.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.ru.movieshows.domain.entity.GenreEntity
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
