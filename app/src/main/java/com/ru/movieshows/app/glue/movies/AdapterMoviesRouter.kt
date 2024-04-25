package com.ru.movieshows.app.glue.movies

import android.util.Log
import com.ru.movieshows.app.R
import com.ru.movieshows.movies.MoviesRouter
import com.ru.movieshows.movies.domain.entities.Movie
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
    }

    override fun launchPopularMovies() {
    }

    override fun launchTopRatedMovies() {
    }

    override fun launchMovieSearch() {
        globalNavComponentRouter.launch(R.id.movieSearchFragment)
    }
}