package com.ru.movieshows.presentation.viewmodel.movies

import androidx.annotation.StringRes
import com.ru.movieshows.domain.entity.GenreEntity
import com.ru.movieshows.domain.entity.MovieEntity

sealed class MoviesState {
    object InPending: MoviesState()

    data class Success(
        val nowPlayingMovies: ArrayList<MovieEntity>,
        val genres: ArrayList<GenreEntity>,
        val upcomingMovies: ArrayList<MovieEntity>,
        val popularMovies: ArrayList<MovieEntity>,
        val topRatedMovies: ArrayList<MovieEntity>
        ): MoviesState()

    data class Failure(
        @StringRes val header: Int? = null,
        @StringRes val error: Int? = null,
    ): MoviesState()
}

sealed class MoviesDiscoverState {
    object InPending: MoviesDiscoverState()
    data class Success(
        val movies: ArrayList<MovieEntity>
    ): MoviesDiscoverState()
    data class Failure(
        @StringRes val error: Int? = null,
    ): MoviesDiscoverState()
}