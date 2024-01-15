package com.ru.movieshows.sources.movies.models

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.ru.movieshows.BuildConfig
import com.ru.movieshows.sources.genres.models.GenreModel
import com.ru.movieshows.sources.movies.entities.MovieDetailsEntity
import com.ru.movieshows.sources.tv_shows.models.ProductionCompanyModel
import java.text.SimpleDateFormat

data class GetMovieDetailsResponseModels(
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
    fun toMovieDetailsEntity(): MovieDetailsEntity {
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
