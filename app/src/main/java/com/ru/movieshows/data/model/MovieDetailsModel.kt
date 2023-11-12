package com.ru.movieshows.data.model

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.ru.movieshows.BuildConfig
import com.ru.movieshows.domain.entity.MovieDetailsEntity
import java.text.SimpleDateFormat

data class MovieDetailsModel(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("genres")
    val genres: ArrayList<GenreModel>,
    @SerializedName("release_date")
    val releaseDate: String?,
    @SerializedName("overview")
    val overview: String?,
    @SerializedName("backdrop_path")
    val backDrop: String?,
    @SerializedName("poster_path")
    val poster: String?,
    @SerializedName("vote_average")
    val rating: Double?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("runtime")
    val runtime: String?,
    @SerializedName("production_companies")
    val productionCompanies: ArrayList<ProductionCompanyModel>?,
) {
    @SuppressLint("SimpleDateFormat")
    fun toEntity(): MovieDetailsEntity {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val date = if(releaseDate != null) simpleDateFormat.parse(releaseDate) else null
        val productionCompanies = this.productionCompanies?.let { it ->
            ArrayList(it.map { it.toEntity() })
        }

        return MovieDetailsEntity(
            id,
            ArrayList(genres.map { it.toEntity() }),
            date,
            overview,
            if(this.backDrop != null) BuildConfig.TMDB_IMAGE_URL + this.backDrop else null,
            if(this.poster != null) BuildConfig.TMDB_IMAGE_URL + this.poster else null,
            rating,
            title,
            runtime,
            productionCompanies,
        )
    }
}
