package com.ru.movieshows.sources.tvshows.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.ru.movieshows.sources.tvshows.entities.CreatorEntity
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
    fun toEntity(imageUrl: String): CreatorEntity {
        val photo = if(this.photo != null) imageUrl + this.photo else null

        return CreatorEntity(
            id,
            name,
            creditId,
            photo
        )
    }
}