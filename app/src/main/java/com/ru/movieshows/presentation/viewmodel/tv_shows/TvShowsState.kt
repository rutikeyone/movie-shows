package com.ru.movieshows.presentation.viewmodel.tv_shows

import androidx.annotation.StringRes
import com.ru.movieshows.domain.entity.TvShowsEntity
import com.ru.movieshows.presentation.viewmodel.movies.MoviesState

sealed class TvShowsState {
    object InPending: TvShowsState()

    data class Success(
        val trendingMovies: ArrayList<TvShowsEntity>,
    ) : TvShowsState()

    data class Failure(
        @StringRes val header: Int? = null,
        @StringRes val error: Int? = null,
    ): TvShowsState()
}