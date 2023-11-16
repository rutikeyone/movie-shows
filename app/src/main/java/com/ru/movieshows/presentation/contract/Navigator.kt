package com.ru.movieshows.presentation.contract

import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ru.movieshows.domain.entity.ReviewEntity
import com.ru.movieshows.domain.entity.VideoEntity

fun Fragment.navigator(): Navigator {
    return requireActivity() as Navigator
}

interface Navigator {

    fun getToolbar(): Toolbar?

    fun getBottomNavigationView(): BottomNavigationView?

    fun navigateToPopularMovies()

    fun navigateToTopRatedMovies()

    fun navigateToUpcomingMovies()

    fun navigateToMovieDetails(id: Int)

    fun navigateToMovieSearch()

    fun navigateToTvShowsSearch()

    fun navigateToTvShowDetails(id: Int)

    fun navigateToAirTvShows()

    fun navigateToTopRatedTvShows()

    fun navigateToPopularTvShows()

    fun navigateToReviews(reviews: ArrayList<ReviewEntity>, movieId: Int)

    fun navigateToVideo(video: VideoEntity)

    fun pop()

}