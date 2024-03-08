package com.ru.movieshows.data.movies.models

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.ru.movieshows.data.genres.models.GenreModel
import com.ru.movieshows.data.movies.converters.ImageJsonDeserializer

data class MovieDetailsModel(
    val id: Int?,
    val genres: List<GenreModel>,
    @SerializedName("release_date")
    val releaseDate: String?,
    val overview: String?,
    @SerializedName("backdrop_path")
    @JsonAdapter(value = ImageJsonDeserializer::class)
    val backDrop: String?,
    @SerializedName("poster_path")
    @JsonAdapter(value = ImageJsonDeserializer::class)
    val poster: String?,
    @SerializedName("vote_average")
    val rating: Double?,
    val title: String?,
    val runtime: String?,
    @SerializedName("production_companies")
    val productionCompanies: List<ProductionCompanyModel>?,
)