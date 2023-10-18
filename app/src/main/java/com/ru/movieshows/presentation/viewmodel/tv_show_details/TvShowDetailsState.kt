package com.ru.movieshows.presentation.viewmodel.tv_show_details

import androidx.annotation.StringRes
import com.ru.movieshows.domain.entity.TvShowDetailsEntity
import com.ru.movieshows.domain.entity.TvShowsEntity

sealed class TvShowDetailsState {
    object Pure: TvShowDetailsState()
    object InPending : TvShowDetailsState()
    data class Success(
        val tvShow: TvShowDetailsEntity,
    ): TvShowDetailsState()
    data class Failure(
        @StringRes val header: Int? = null,
        @StringRes val error: Int? = null,
    ): TvShowDetailsState()
}