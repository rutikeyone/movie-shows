package com.ru.movieshows.app.presentation.viewmodel.movies.state

import androidx.annotation.StringRes
import com.ru.movieshows.sources.movies.entities.MovieDetailsEntity
import com.ru.movieshows.sources.movies.entities.MovieEntity
import com.ru.movieshows.sources.movies.entities.ReviewEntity
import com.ru.movieshows.sources.movies.entities.VideoEntity

sealed class MovieDetailsState {
    object InPending : MovieDetailsState()
    data class Success(
        val movieDetails: MovieDetailsEntity,
        val similarMovies: ArrayList<MovieEntity>,
        val videos: ArrayList<VideoEntity>,
        val reviews: ArrayList<ReviewEntity>,
    ): MovieDetailsState()
    data class Failure(
        @StringRes val header: Int? = null,
        @StringRes val error: Int? = null,
    ): MovieDetailsState()
}