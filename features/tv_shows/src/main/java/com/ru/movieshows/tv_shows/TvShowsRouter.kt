package com.ru.movieshows.tv_shows

import com.ru.movieshows.tv_shows.domain.entities.TvShow

interface TvShowsRouter {

    fun launchTvShowSearch()

    fun launchTvShowsDetails(tvShow: TvShow)

    fun launchAirTvShows()

    fun launchTopRatedTvShows()

    fun launchPopularTvShows()

}