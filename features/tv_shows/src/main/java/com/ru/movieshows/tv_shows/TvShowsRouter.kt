package com.ru.movieshows.tv_shows

import com.ru.movieshows.tv_shows.domain.entities.TvShow
import com.ru.movieshows.tv_shows.domain.entities.Video

interface TvShowsRouter {

    fun launchTvShowSearch()

    fun launchTvShowsDetails(tvShow: TvShow)

    fun launchOnTheAirTvShows()

    fun launchTopRatedTvShows()

    fun launchPopularTvShows()

    fun launchToEpisodes(seriesId: String, seasonNumber: String)

    fun launchToTvShowReviews()

    fun launchVideo(video: Video)

}