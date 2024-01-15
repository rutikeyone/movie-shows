package com.ru.movieshows.app.presentation.viewmodel.tv_shows.state

import com.ru.movieshows.sources.tv_shows.entities.TvShowsEntity

data class TvShowSearchState(
    val language: String,
    val query: String,
    val trendingTvShows: ArrayList<TvShowsEntity>?,
    val isSearchMode: Boolean,
) {
    val trendingTvShowsInPending: Boolean
        get() = trendingTvShows == null
}