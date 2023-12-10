package com.ru.movieshows.presentation.viewmodel.tv_shows.state

import androidx.annotation.StringRes
import com.ru.movieshows.domain.entity.TvShowsEntity

sealed class TvShowsState {
    object InPending: TvShowsState()
    data class Success(
        val trendingMovies: ArrayList<TvShowsEntity>,
        val onAirTvShows: ArrayList<TvShowsEntity>,
        val topRatedTvShows: ArrayList<TvShowsEntity>,
        val popularTvShows: ArrayList<TvShowsEntity>,
    ) : TvShowsState()
    data class Failure(
        @StringRes val header: Int? = null,
        @StringRes val error: Int? = null,
    ): TvShowsState()
}