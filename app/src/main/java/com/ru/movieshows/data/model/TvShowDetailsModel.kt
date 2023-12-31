package com.ru.movieshows.data.model

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.ru.movieshows.BuildConfig
import com.ru.movieshows.domain.entity.TvShowDetailsEntity
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
    fun toEntity(): TvShowDetailsEntity {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val date = if(firstAirDate != null) simpleDateFormat.parse(firstAirDate) else null
        val productionCompanies = this.productionCompanies?.let { it ->
            ArrayList(it.map { it.toEntity() })
        }

        return TvShowDetailsEntity(
            id,
            genres?.map { it.toEntity() }?.let { ArrayList(it) },
            date,
            overview,
            if(this.backDrop != null) BuildConfig.TMDB_IMAGE_URL + this.backDrop else null,
            if(this.poster != null) BuildConfig.TMDB_IMAGE_URL + this.poster else null,
            rating,
            name,
            numberOfEpisodes,
            numberOfSeasons,
            createdBy?.map { it.toEntity() }?.let { ArrayList(it) },
            seasons?.map { it.toEntity() }?.let { ArrayList(it) },
            productionCompanies
        )
    }
}