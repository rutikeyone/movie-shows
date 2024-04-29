package com.ru.movieshows.tv_shows.presentation.details

import com.ru.movieshows.core.presentation.BaseViewModel
import com.ru.movieshows.tv_shows.TvShowsRouter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class TvShowDetailsViewModel @AssistedInject constructor(
    @Assisted private val args: TvShowDetailsFragment.Screen,
    private val router: TvShowsRouter,
) : BaseViewModel() {

    fun launchVideo() {}

    fun launchToTvShowReviews() {}

    fun launchToEpisodes(seriesId: String, seasonNumber: String) {}

    @AssistedFactory
    interface Factory {
        fun create(args: TvShowDetailsFragment.Screen): TvShowDetailsViewModel
    }

}