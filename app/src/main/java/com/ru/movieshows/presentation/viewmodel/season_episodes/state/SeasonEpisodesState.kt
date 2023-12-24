package com.ru.movieshows.presentation.viewmodel.season_episodes.state

import androidx.annotation.StringRes
import com.ru.movieshows.domain.entity.EpisodeEntity

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