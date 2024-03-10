package com.ru.movieshows.data.movies.models

import com.google.gson.annotations.SerializedName
import com.ru.ershov.data.core.ImagePreviewMapper
import javax.inject.Inject

data class MovieModel(
    val id: Int?,
    @SerializedName("vote_average")
    val rating: Double?,
    val title: String?,
    @SerializedName("backdrop_path")
    val backDropPath: String?,
    @SerializedName("poster_path")
    val posterPath: String?,
    val overview: String?,
) {

    @Inject
    lateinit var imagePreviewMapper: ImagePreviewMapper

    val backDrop: String? get() {
        return imagePreviewMapper.toPreviewImage(backDropPath)
    }

    val poster: String? get() {
        return imagePreviewMapper.toPreviewImage(posterPath)
    }

}