package com.ru.movieshows.presentation.viewmodel.movies

import androidx.annotation.StringRes
import com.ru.movieshows.domain.entity.MovieEntity

sealed class MoviesState {
    object InPending: MoviesState()

    data class Success(
        val nowPlayingMovies: ArrayList<MovieEntity>
    ): MoviesState()

    data class Failure(@StringRes val error: Int): MoviesState()
}