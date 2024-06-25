package com.ru.movieshows.app.glue.movies

import com.ru.movieshows.app.R
import com.ru.movieshows.movies.MoviesRouter
import com.ru.movieshows.movies.domain.entities.Movie
import com.ru.movieshows.movies.domain.entities.Video
import com.ru.movieshows.movies.presentation.details.MovieDetailsFragment
import com.ru.movieshows.movies.presentation.reviews.MovieReviewsFragment
import com.ru.movieshows.navigation.GlobalNavComponentRouter
import com.ru.movieshows.video.presentation.VideoPlayerActivity
import javax.inject.Inject

class AdapterMoviesRouter @Inject constructor(
    private val globalNavComponentRouter: GlobalNavComponentRouter,
) : MoviesRouter {

    override fun launchMovieDetails(id: Int?) {
        val movieId = id ?: return
        val args = MovieDetailsFragment.Screen(movieId)
        globalNavComponentRouter.launch(R.id.movieDetailsFragment, args)
    }

    override fun launchUpcomingMovies() {
        globalNavComponentRouter.launch(R.id.upcomingMoviesFragment)
    }

    override fun launchPopularMovies() {
        globalNavComponentRouter.launch(R.id.popularMoviesFragment)
    }

    override fun launchTopRatedMovies() {
        globalNavComponentRouter.launch(R.id.topRatedMoviesFragment)
    }

    override fun launchMovieSearch() {
        globalNavComponentRouter.launch(R.id.movieSearchFragment)
    }

    override fun launchVideo(key: String) {
        val args = VideoPlayerActivity.Screen(key)
        val useRootNavController = true

        globalNavComponentRouter.launch(R.id.videoPlayerActivity, args, useRootNavController)
    }

    override fun launchMovieReviews(id: String?) {
        val movieId = id ?: return
        val args = MovieReviewsFragment.Screen(movieId)

        globalNavComponentRouter.launch(R.id.movieReviewsFragment, args)
    }

}