package com.ru.movieshows.app.presentation.viewmodel.movies.state

import androidx.annotation.StringRes
import com.ru.movieshows.sources.genres.entities.GenreEntity
import com.ru.movieshows.sources.movies.entities.MovieEntity

sealed class MovieState {

    object Empty: MovieState()

    object Pending: MovieState()

    data class Success(
        val nowPlayingMovies: ArrayList<MovieEntity>,
        val genres: ArrayList<GenreEntity>,
        val upcomingMovies: ArrayList<MovieEntity>,
        val popularMovies: ArrayList<MovieEntity>,
        val topRatedMovies: ArrayList<MovieEntity>
        ): MovieState()

    data class Failure(
        @StringRes val header: Int? = null,
        @StringRes val error: Int? = null,
    ): MovieState()

}
