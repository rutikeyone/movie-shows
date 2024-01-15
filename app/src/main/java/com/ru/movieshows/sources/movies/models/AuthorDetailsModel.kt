package com.ru.movieshows.sources.movies.models

import com.google.gson.annotations.SerializedName
import com.ru.movieshows.BuildConfig
import com.ru.movieshows.sources.movies.entities.AuthorDetailsEntity

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
