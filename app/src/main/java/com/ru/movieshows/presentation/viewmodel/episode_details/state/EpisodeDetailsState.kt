package com.ru.movieshows.presentation.viewmodel.episode_details.state

import androidx.annotation.StringRes
import com.ru.movieshows.domain.entity.EpisodeEntity

data class EpisodeDetailsState(
    val seriesId: String,
    val seasonNumber: String,
    val episodeNumber: Int,
    val title: String = "",
    val status: EpisodeDetailsStatus = EpisodeDetailsStatus.Pure,
)

sealed class EpisodeDetailsStatus {
    object Pure : EpisodeDetailsStatus()
    object InPending : EpisodeDetailsStatus()
    data class Success(
        val episode: EpisodeEntity,
    ) : EpisodeDetailsStatus()
    data class Failure(
        @StringRes val header: Int? = null,
        @StringRes val error: Int? = null,
    ): EpisodeDetailsStatus()
}