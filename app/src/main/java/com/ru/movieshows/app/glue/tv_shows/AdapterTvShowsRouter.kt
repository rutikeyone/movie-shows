package com.ru.movieshows.app.glue.tv_shows

import com.ru.movieshows.app.R
import com.ru.movieshows.navigation.GlobalNavComponentRouter
import com.ru.movieshows.tv_shows.TvShowsRouter
import com.ru.movieshows.tv_shows.domain.entities.TvShow
import com.ru.movieshows.tv_shows.domain.entities.Video
import com.ru.movieshows.tv_shows.presentation.details.TvShowDetailsFragment
import javax.inject.Inject

class AdapterTvShowsRouter @Inject constructor(
    private val globalNavComponentRouter: GlobalNavComponentRouter,
) : TvShowsRouter {

    override fun launchTvShowSearch() {
        globalNavComponentRouter.launch(R.id.tvShowSearchFragment)
    }

    override fun launchTvShowsDetails(tvShow: TvShow) {
        val id = tvShow.id ?: return
        val args = TvShowDetailsFragment.Screen(id)
        globalNavComponentRouter.launch(R.id.tvShowDetailsFragment, args)
    }

    override fun launchOnTheAirTvShows() {
        globalNavComponentRouter.launch(R.id.onTheAirTvShowsFragment)
    }

    override fun launchTopRatedTvShows() {
        globalNavComponentRouter.launch(R.id.topRatedTvShowsFragment)
    }

    override fun launchPopularTvShows() {
        globalNavComponentRouter.launch(R.id.popularTvShowsFragment)
    }

    override fun launchToEpisodes(seriesId: String, seasonNumber: String) {}

    override fun launchToTvShowReviews() {}

    override fun launchVideo(video: Video) {}

}