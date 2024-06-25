package com.ru.movieshows.app.glue.movies.mappers

import com.ru.movieshows.app.formatters.ImageUrlFormatter
import com.ru.movieshows.data.movies.models.MovieModel
import com.ru.movieshows.movies.domain.entities.Movie
import javax.inject.Inject

class MovieMapper @Inject constructor(
    private val imageUrlFormatter: ImageUrlFormatter,
) {

    fun toMovie(model: MovieModel): Movie {
        return Movie(
            id = model.id,
            rating = model.rating,
            title = model.title,
            backDropPath = imageUrlFormatter.toImageUrl(model.backDropPath),
            posterPath = imageUrlFormatter.toImageUrl(model.posterPath),
            overview = model.overview,
        )
    }

    fun toMovieModel(movie: Movie): MovieModel {
        return MovieModel(
            id = movie.id,
            rating = movie.rating,
            title = movie.title,
            backDropPath = movie.backDropPath,
            posterPath = movie.posterPath,
            overview = movie.overview,
        )
    }

}