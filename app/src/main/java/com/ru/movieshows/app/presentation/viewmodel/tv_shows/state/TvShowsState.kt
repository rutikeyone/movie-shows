package com.ru.movieshows.app.presentation.viewmodel.tv_shows.state

import androidx.annotation.StringRes
import com.ru.movieshows.sources.tv_shows.entities.TvShowsEntity

sealed class TvShowsState {

    object Empty : TvShowsState()

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