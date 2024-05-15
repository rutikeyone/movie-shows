package com.ru.movieshows.app.glue.movies

import com.ru.movieshows.app.R
import com.ru.movieshows.movies.MoviesRouter
import com.ru.movieshows.movies.domain.entities.Movie
import com.ru.movieshows.movies.domain.entities.Video
import com.ru.movieshows.movies.presentation.details.MovieDetailsFragment
import com.ru.movieshows.navigation.GlobalNavComponentRouter
import javax.inject.Inject

class AdapterMoviesRouter @Inject constructor(
    private val globalNavComponentRouter: GlobalNavComponentRouter,
) : MoviesRouter {

    override fun launchMovieDetails(movie: Movie) {
        val id = movie.id ?: return
        val args = MovieDetailsFragment.Screen(id)
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

    override fun launchVideo(video: Video) {
    }

    override fun launchToReviews() {
    }
}