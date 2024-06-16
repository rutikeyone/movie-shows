package com.ru.movieshows.tv_shows

import androidx.fragment.app.FragmentManager
import com.ru.movieshows.tv_shows.domain.entities.TvShow
import com.ru.movieshows.tv_shows.domain.entities.Video

interface TvShowsRouter {

    fun launchTvShowSearch()

    fun launchTvShowsDetails(tvShow: TvShow)

    fun launchOnTheAirTvShows()

    fun launchTopRatedTvShows()

    fun launchPopularTvShows()

    fun launchToEpisodes(seriesId: String, seasonNumber: String)

    fun launchToTvShowReviews(tvShowId: String?)

    fun launchVideo(video: Video)

    fun launchSeasonDetailsBottomSheetDialog(
        childFragmentManager: FragmentManager,
        seasonNumber: String,
        seriesId: String,
    )

    fun launchEpisodes(
        seriesId: String,
        seasonNumber: String,
    )

    fun launchEpisodeDetails()

}