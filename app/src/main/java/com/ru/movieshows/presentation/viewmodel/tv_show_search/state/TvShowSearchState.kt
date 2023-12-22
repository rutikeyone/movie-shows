package com.ru.movieshows.presentation.viewmodel.tv_show_search.state

import com.ru.movieshows.domain.entity.TvShowsEntity

data class TvShowSearchState(
    val language: String,
    val query: String,
    val trendingTvShows: ArrayList<TvShowsEntity>?,
    val isSearchMode: Boolean,
) {
    val trendingTvShowsInPending: Boolean
        get() = trendingTvShows == null
}