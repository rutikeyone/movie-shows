package com.ru.movieshows.sources.movies.models

import com.google.gson.annotations.SerializedName
import com.ru.movieshows.sources.movies.entities.AuthorDetailsEntity

data class AuthorDetailsModel(
    @SerializedName("avatar_path")
    val avatar: String?,
    @SerializedName("rating")
    val rating: Double?
) {
    fun toEntity(imageUrl: String): AuthorDetailsEntity {
        return AuthorDetailsEntity(
            if(this.avatar != null) imageUrl + this.avatar else null,
            rating
        )
    }
}
