package com.ru.movieshows.data.movies.models

import com.google.gson.annotations.SerializedName
import com.ru.ershov.data.core.ImagePreviewMapper
import com.ru.movieshows.data.genres.models.GenreModel
import javax.inject.Inject

data class MovieDetailsModel(
    val id: Int?,
    val genres: List<GenreModel>,
    @SerializedName("release_date")
    val releaseDate: String?,
    val overview: String?,
    @SerializedName("backdrop_path")
    val backDropPath: String?,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("vote_average")
    val rating: Double?,
    val title: String?,
    val runtime: String?,
    @SerializedName("production_companies")
    val productionCompanies: List<ProductionCompanyModel>?,
) {

    @Inject
    lateinit var previewMapper: ImagePreviewMapper

    val backDrop: String? get() {
        return previewMapper.toPreviewImage(backDropPath)
    }

    val poster: String? get() {
        return previewMapper.toPreviewImage(posterPath)
    }

}