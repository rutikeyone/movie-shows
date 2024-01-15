package com.ru.movieshows.sources.movies.models

import com.google.gson.annotations.SerializedName
import com.ru.movieshows.BuildConfig
import com.ru.movieshows.sources.movies.entities.MovieEntity

data class MovieModel(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("vote_average")
    val rating: Double?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("backdrop_path")
    val backDrop: String?,
    @SerializedName("poster_path")
    val poster: String?,
    @SerializedName("overview")
    val overview: String?
) {
    fun toEntity(): MovieEntity = MovieEntity(
        id,
        rating,
        title,
        if(this.backDrop != null) BuildConfig.TMDB_IMAGE_URL + this.backDrop else null,
        if(this.poster != null) BuildConfig.TMDB_IMAGE_URL + this.poster else null,
        overview
    )
}
