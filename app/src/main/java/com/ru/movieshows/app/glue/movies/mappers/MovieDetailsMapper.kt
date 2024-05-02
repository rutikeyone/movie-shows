package com.ru.movieshows.app.glue.movies.mappers

import android.annotation.SuppressLint
import com.ru.movieshows.app.formatters.ImageUrlFormatter
import com.ru.movieshows.data.movies.models.MovieDetailsModel
import com.ru.movieshows.movies.domain.entities.MovieDetails
import java.text.SimpleDateFormat
import javax.inject.Inject

class MovieDetailsMapper @Inject constructor(
    private val imageUrlFormatter: ImageUrlFormatter,
    private val movieGenreMapper: MovieGenreMapper,
    private val productionCompanyMapper: ProductionCompanyMapper,
) {

    @SuppressLint("SimpleDateFormat")
    fun toMovieDetails(model: MovieDetailsModel): MovieDetails {
        val simpleDateFormatter = SimpleDateFormat("yyyy-MM-dd")

        val modelReleaseDate = model.releaseDate
        val releaseDate = if(modelReleaseDate != null) {
            simpleDateFormatter.parse(modelReleaseDate)
        } else {
            null
        }

        return MovieDetails(
            id = model.id,
            genres = model.genres.map { movieGenreMapper.toGenre(it) },
            releaseDate = releaseDate,
            overview = model.overview,
            backDropPath = imageUrlFormatter.toImageUrl(model.backDropPath),
            posterPath = imageUrlFormatter.toImageUrl(model.posterPath),
            rating = model.rating,
            title = model.title,
            runtime = model.runtime,
            productionCompanies = model.productionCompanies?.map {
                productionCompanyMapper.toProductionCompany(it)
            }
        );
    }

}