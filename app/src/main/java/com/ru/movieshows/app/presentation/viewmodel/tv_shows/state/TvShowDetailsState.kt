package com.ru.movieshows.app.presentation.viewmodel.tv_shows.state

import androidx.annotation.StringRes
import com.ru.movieshows.sources.movies.entities.ReviewEntity
import com.ru.movieshows.sources.movies.entities.VideoEntity
import com.ru.movieshows.sources.tvshows.entities.TvShowDetailsEntity

sealed class TvShowDetailsState {

    object Empty: TvShowDetailsState()

    object InPending : TvShowDetailsState()

    data class Success(
        val tvShow: TvShowDetailsEntity,
        val videos: ArrayList<VideoEntity>,
        val reviews: ArrayList<ReviewEntity>,
    ): TvShowDetailsState()

    data class Failure(
        @StringRes val header: Int? = null,
        @StringRes val error: Int? = null,
    ): TvShowDetailsState()

}