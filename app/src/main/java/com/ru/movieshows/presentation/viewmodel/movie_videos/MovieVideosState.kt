package com.ru.movieshows.presentation.viewmodel.movie_videos

import androidx.annotation.StringRes
import com.ru.movieshows.domain.entity.VideoEntity
import com.ru.movieshows.presentation.viewmodel.movie_details.MovieDetailsState

sealed class MovieVideosState {
    object InPending: MovieVideosState()
    data class Success(
        val videos: ArrayList<VideoEntity>
    ) : MovieVideosState()
    object SuccessEmpty : MovieVideosState()
    data class Failure(
        @StringRes val header: Int? = null,
        @StringRes val error: Int? = null,
    ) : MovieVideosState()
}