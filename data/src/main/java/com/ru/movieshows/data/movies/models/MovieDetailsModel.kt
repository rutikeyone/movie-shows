package com.ru.movieshows.data.movies.models

import com.google.gson.annotations.SerializedName
import com.ru.movieshows.data.genres.models.GenreModel

data class MovieDetailsModel(
    @SerializedName("id") val id: Int?,
    @SerializedName("genres") val genres: List<GenreModel>,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("overview") val overview: String?,
    @SerializedName("backdrop_path") val backDropPath: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("vote_average") val rating: Double?,
    @SerializedName("title") val title: String?,
    @SerializedName("runtime") val runtime: String?,
    @SerializedName("production_companies") val productionCompanies: List<ProductionCompanyModel>?,
)