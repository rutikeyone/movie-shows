package com.ru.movieshows.app.presentation.viewmodel.movies.state

import androidx.annotation.StringRes
import com.ru.movieshows.sources.movies.entities.MovieEntity

sealed class DiscoverMoviesState {

    object Empty : DiscoverMoviesState()

    object Pending: DiscoverMoviesState()

    data class Success(
        val movies: ArrayList<MovieEntity>
    ): DiscoverMoviesState()

    data class Failure(
        @StringRes val error: Int? = null,
    ): DiscoverMoviesState()

}