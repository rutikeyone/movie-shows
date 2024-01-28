package com.ru.movieshows.app.presentation.viewmodel.episode.state

import androidx.annotation.StringRes
import com.ru.movieshows.sources.tv_shows.entities.EpisodeEntity
import com.ru.movieshows.sources.tv_shows.entities.SeasonEntity

data class EpisodeDetailsState(
    val seriesId: String,
    val seasonNumber: String,
    val episodeNumber: Int,
    val title: String = "",
    val status: EpisodeDetailsStatus = EpisodeDetailsStatus.Empty,
)

sealed class EpisodeDetailsStatus {

    object Empty : EpisodeDetailsStatus()

    object InPending : EpisodeDetailsStatus()

    data class Success(
        val season: SeasonEntity,
        val episode: EpisodeEntity,
    ) : EpisodeDetailsStatus()

    data class Failure(
        @StringRes val header: Int? = null,
        @StringRes val error: Int? = null,
    ): EpisodeDetailsStatus()

}