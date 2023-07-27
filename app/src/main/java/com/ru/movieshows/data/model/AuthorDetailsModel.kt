package com.ru.movieshows.data.model

import com.google.gson.annotations.SerializedName
import com.ru.movieshows.BuildConfig
import com.ru.movieshows.domain.entity.AuthorDetailsEntity

data class AuthorDetailsModel(
    @SerializedName("avatar_path")
    val avatar: String?,
    @SerializedName("rating")
    val rating: Double?
) {
    fun toEntity(): AuthorDetailsEntity = AuthorDetailsEntity(
        if(this.avatar != null) BuildConfig.TMDB_IMAGE_URL + this.avatar else null,
        rating
    )
}
