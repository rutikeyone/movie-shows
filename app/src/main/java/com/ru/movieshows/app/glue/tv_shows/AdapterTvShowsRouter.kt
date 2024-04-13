package com.ru.movieshows.app.glue.tv_shows

import com.ru.movieshows.tv_shows.domain.entities.TvShow
import com.ru.movieshows.tv_shows.presentation.TvShowsRouter
import javax.inject.Inject

class AdapterTvShowsRouter @Inject constructor() : TvShowsRouter {

    override fun launchTvShowSearch() {}

    override fun launchTvShowsDetails(tvShow: TvShow) {}

    override fun launchAirTvShows() {}

    override fun launchTopRatedTvShows() {}

    override fun launchPopularTvShows() {}

}