package com.ru.movieshows.sources.tv_shows.models

import com.google.gson.annotations.SerializedName
import com.ru.movieshows.BuildConfig
import com.ru.movieshows.sources.tv_shows.entities.TvShowsEntity

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
    fun toEntity(): TvShowsEntity = TvShowsEntity(
        id,
        rating,
        name,
        if(this.backDrop != null) BuildConfig.TMDB_IMAGE_URL + this.backDrop else null,
        if(this.poster != null) BuildConfig.TMDB_IMAGE_URL + this.poster else null,
        overview
    )
}