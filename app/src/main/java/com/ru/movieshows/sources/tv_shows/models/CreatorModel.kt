package com.ru.movieshows.sources.tv_shows.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.ru.movieshows.BuildConfig
import com.ru.movieshows.sources.tv_shows.entities.CreatorEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class CreatorModel(
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("credit_id")
    val creditId: String?,
    @SerializedName("profile_path")
    val photo: String?,
): Parcelable {
    fun toEntity(imageUrl: String): CreatorEntity = CreatorEntity(
        id,
        name,
        creditId,
        if(this.photo != null) imageUrl + this.photo else null
    )
}