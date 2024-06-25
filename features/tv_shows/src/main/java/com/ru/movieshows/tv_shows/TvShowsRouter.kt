package com.ru.movieshows.tv_shows

import androidx.fragment.app.FragmentManager
import com.ru.movieshows.tv_shows.domain.entities.TvShow
import com.ru.movieshows.tv_shows.domain.entities.Video

interface TvShowsRouter {

    fun launchTvShowSearch()

    fun launchTvShowsDetails(id: String?)

    fun launchOnTheAirTvShows()

    fun launchTopRatedTvShows()

    fun launchPopularTvShows()

    fun launchToEpisodes(seriesId: String, seasonNumber: String)

    fun launchToTvShowReviews(tvShowId: String?)

    fun launchSeasonDetailsBottomSheetDialog(
        childFragmentManager: FragmentManager,
        seasonNumber: String,
        seriesId: String,
    )

    fun launchEpisodes(
        seriesId: String,
        seasonNumber: String,
    )

    fun launchEpisodeDetails(
        seriesId: String,
        seasonNumber: String,
        episodeNumber: Int,
    )

    fun launchPersonDetailsDialog(
        id: String?,
        childFragmentManager: FragmentManager,
    )

    fun launchVideo(
        key: String,
    )

}