package com.ru.movieshows.presentation.viewmodel.movie_details

import androidx.annotation.StringRes
import com.ru.movieshows.domain.entity.MovieDetailsEntity
import com.ru.movieshows.domain.entity.MovieEntity
import com.ru.movieshows.domain.entity.ReviewEntity

sealed class MovieDetailsState {
    object InPending : MovieDetailsState()
    data class Success(
        val movieDetails: MovieDetailsEntity,
        val similarMovies: ArrayList<MovieEntity>,
        val reviews: ArrayList<ReviewEntity>
    ): MovieDetailsState()
    data class Failure(
        @StringRes val header: Int? = null,
        @StringRes val error: Int? = null,
    ): MovieDetailsState()
}