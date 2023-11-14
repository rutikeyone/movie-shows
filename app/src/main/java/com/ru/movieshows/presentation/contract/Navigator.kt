package com.ru.movieshows.presentation.contract

import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.ru.movieshows.domain.entity.ReviewEntity

fun Fragment.navigator(): Navigator {
    return requireActivity() as Navigator
}

interface Navigator {

    fun getToolbar(): Toolbar?

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

    fun pop()

}