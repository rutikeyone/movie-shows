package com.ru.movieshows.presentation.viewmodel.movies.state

import androidx.annotation.StringRes
import com.ru.movieshows.domain.entity.MovieEntity

sealed class DiscoverMoviesState {
    object InPending: DiscoverMoviesState()
    data class Success(
        val movies: ArrayList<MovieEntity>
    ): DiscoverMoviesState()
    data class Failure(
        @StringRes val error: Int? = null,
    ): DiscoverMoviesState()
}