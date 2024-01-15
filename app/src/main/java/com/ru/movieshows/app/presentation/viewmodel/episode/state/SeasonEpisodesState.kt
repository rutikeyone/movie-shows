package com.ru.movieshows.app.presentation.viewmodel.episode.state

import androidx.annotation.StringRes
import com.ru.movieshows.sources.tv_shows.entities.EpisodeEntity

sealed class SeasonEpisodesState {
    object InPending: SeasonEpisodesState()
    data class Success(
        val episodes: ArrayList<EpisodeEntity>,
    ) : SeasonEpisodesState()
    object SuccessEmpty: SeasonEpisodesState()
    data class Failure(
        @StringRes val header: Int? = null,
        @StringRes val error: Int? = null,
    ): SeasonEpisodesState()
}