package com.ru.movieshows.data.tvshows.models

import com.google.gson.annotations.SerializedName
import com.ru.movieshows.data.genres.models.GenreModel
import com.ru.movieshows.data.movies.models.ProductionCompanyModel

data class TvShowDetailsModel(
    val id: Int?,
    val genres: List<GenreModel>?,
    @SerializedName("first_air_date")
    val firstAirDate: String?,
    val overview: String?,
    @SerializedName("backdrop_path")
    val backDropPath: String?,
    @SerializedName("poster_path")
    val poster: String?,
    @SerializedName("vote_average")
    val rating: Double?,
    val name: String?,
    @SerializedName("number_of_episodes")
    val numberOfEpisodes: Int?,
    @SerializedName("number_of_seasons")
    val numberOfSeasons: Int?,
    @SerializedName("created_by")
    val createdBy: List<CreatorModel>?,
    val seasons: List<SeasonModel>?,
    @SerializedName("production_companies")
    val productionCompanies: List<ProductionCompanyModel>?,
)