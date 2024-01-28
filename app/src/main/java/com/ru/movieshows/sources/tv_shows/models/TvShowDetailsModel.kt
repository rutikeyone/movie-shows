package com.ru.movieshows.sources.tv_shows.models

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.ru.movieshows.BuildConfig
import com.ru.movieshows.sources.genres.models.GenreModel
import com.ru.movieshows.sources.tv_shows.entities.TvShowDetailsEntity
import java.text.SimpleDateFormat

data class TvShowDetailsModel(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("genres")
    val genres: ArrayList<GenreModel>?,
    @SerializedName("first_air_date")
    val firstAirDate: String?,
    @SerializedName("overview")
    val overview: String?,
    @SerializedName("backdrop_path")
    val backDrop: String?,
    @SerializedName("poster_path")
    val poster: String?,
    @SerializedName("vote_average")
    val rating: Double?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("number_of_episodes")
    val numberOfEpisodes: Int?,
    @SerializedName("number_of_seasons")
    val numberOfSeasons: Int?,
    @SerializedName("created_by")
    val createdBy: ArrayList<CreatorModel>?,
    @SerializedName("seasons")
    val seasons: ArrayList<SeasonModel>?,
    @SerializedName("production_companies")
    val productionCompanies: ArrayList<ProductionCompanyModel>?,
){
    @SuppressLint("SimpleDateFormat")
    fun toEntity(imageUrl: String): TvShowDetailsEntity {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val date = if(firstAirDate != null) simpleDateFormat.parse(firstAirDate) else null
        val backDropUrl = if(this.backDrop != null) imageUrl + this.backDrop else null
        val posterUrl = if(this.poster != null) imageUrl + this.poster else null
        val productionCompanies = this.productionCompanies?.let { it ->
            ArrayList(it.map { it.toEntity() })
        }

        return TvShowDetailsEntity(
            id,
            genres?.map { it.toEntity() }?.let { ArrayList(it) },
            date,
            overview,
            backDropUrl,
            posterUrl,
            rating,
            name,
            numberOfEpisodes,
            numberOfSeasons,
            createdBy?.map { it.toEntity(imageUrl) }?.let { ArrayList(it) },
            seasons?.map { it.toEntity(imageUrl) }?.let { ArrayList(it) },
            productionCompanies
        )
    }
}