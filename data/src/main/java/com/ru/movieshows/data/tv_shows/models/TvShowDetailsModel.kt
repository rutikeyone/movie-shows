package com.ru.movieshows.data.tv_shows.models

import com.google.gson.annotations.SerializedName
import com.ru.movieshows.data.genres.models.GenreModel
import com.ru.movieshows.data.movies.models.ProductionCompanyModel

data class TvShowDetailsModel(
    @SerializedName("id") val id: Int?,
    @SerializedName("genres") val genres: List<GenreModel>?,
    @SerializedName("first_air_date") val firstAirDate: String?,
    @SerializedName("overview") val overview: String?,
    @SerializedName("backdrop_path") val backDropPath: String?,
    @SerializedName("poster_path") val poster: String?,
    @SerializedName("vote_average") val rating: Double?,
    @SerializedName("name") val name: String?,
    @SerializedName("number_of_episodes") val numberOfEpisodes: Int?,
    @SerializedName("number_of_seasons") val numberOfSeasons: Int?,
    @SerializedName("created_by") val createdBy: List<CreatorModel>?,
    @SerializedName("seasons") val seasons: List<SeasonModel>?,
    @SerializedName("production_companies") val productionCompanies: List<ProductionCompanyModel>?,
)