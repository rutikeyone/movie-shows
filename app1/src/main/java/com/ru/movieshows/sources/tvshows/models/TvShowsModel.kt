package com.ru.movieshows.sources.tvshows.models

import com.google.gson.annotations.SerializedName
import com.ru.movieshows.sources.tvshows.entities.TvShowsEntity

data class TvShowsModel(
    @SerializedName("id")
    val id: String?,
    @SerializedName("vote_average")
    val rating: Double?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("backdrop_path")
    val backDrop: String?,
    @SerializedName("poster_path")
    val poster: String?,
    @SerializedName("overview")
    val overview: String?
) {
    fun toEntity(imageUrl: String): TvShowsEntity {
        val backDropUrl = if(this.backDrop != null) imageUrl + this.backDrop else null
        val posterUrl = if(this.poster != null) imageUrl + this.poster else null

        return TvShowsEntity(
            id,
            rating,
            name,
            backDropUrl,
            posterUrl,
            overview
        )
    }
}