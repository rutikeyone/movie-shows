package com.ru.movieshows.presentation.utils

import android.widget.Toast
import androidx.annotation.StringRes
import com.ru.movieshows.domain.entity.ReviewEntity
import com.ru.movieshows.domain.entity.VideoEntity

data class SnackBarIntent(
    @StringRes val message: Int
)

data class DialogIntent(
    @StringRes val title: Int,
    @StringRes val message: Int,
    val setPositiveButton: Pair<Int, () -> Unit>,
)

data class PermissionIntent(
    val permission: String,
)
sealed class NavigationIntent {
    object ToPopularMovies : NavigationIntent()
    object ToTopRatedMovies: NavigationIntent()
    object ToUpcomingMovies: NavigationIntent()
    data class ToMovieDetails(val id: Int): NavigationIntent()
    data class ToTvShowsDetails(val id: Int): NavigationIntent()
    object ToMovieSearch: NavigationIntent()
    object Pop : NavigationIntent()
    object ToTvShowsSearch: NavigationIntent()
    object ToAirTvShows: NavigationIntent()
    object ToTopRatedTvShows: NavigationIntent()
    object ToTopPopularTvShows: NavigationIntent()
    data class ToReviews(val reviews: ArrayList<ReviewEntity>, val movieId: Int): NavigationIntent()
    data class ToVideo(val video: VideoEntity): NavigationIntent()

    companion object {
        fun toPopularMovies() = ToPopularMovies
        fun toTopRatedMovies() = ToTopRatedMovies
        fun toUpcomingMovies() = ToUpcomingMovies
        fun toMovieSearch() = ToMovieSearch
        fun toMovieDetails(id: Int) = ToMovieDetails(id)
        fun pop() = Pop
        fun toTvShowsSearch() = ToTvShowsSearch
        fun toTvShowDetails(id: Int) = ToTvShowsDetails(id)
        fun toAirTvShows() = ToAirTvShows
        fun toTopRatedTvShows() = ToTopRatedTvShows
        fun toTopPopularTvShows() = ToTopPopularTvShows
        fun toReviews(reviews: ArrayList<ReviewEntity>, movieId: Int) = ToReviews(reviews, movieId)
        fun toVideo(video: VideoEntity) = ToVideo(video)
    }
}

data class ToastIntent(
    @StringRes val message: Int,
    val duration: Int = Toast.LENGTH_LONG,
)