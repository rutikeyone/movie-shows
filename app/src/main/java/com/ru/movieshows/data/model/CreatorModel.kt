package com.ru.movieshows.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.ru.movieshows.BuildConfig
import com.ru.movieshows.domain.entity.CreatorEntity
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
    fun toEntity(): CreatorEntity = CreatorEntity(
        id,
        name,
        creditId,
        if(this.photo != null) BuildConfig.TMDB_IMAGE_URL + this.photo else null,

        )
}