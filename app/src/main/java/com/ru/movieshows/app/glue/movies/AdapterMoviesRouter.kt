package com.ru.movieshows.app.glue.movies

import android.util.Log
import com.ru.movieshows.movies.MoviesRouter
import com.ru.movieshows.movies.domain.entities.Movie
import com.ru.movieshows.navigation.GlobalNavComponentRouter
import javax.inject.Inject

class AdapterMoviesRouter @Inject constructor(
    private val globalNavComponentRouter: GlobalNavComponentRouter,
) : MoviesRouter {
    override fun launchMovieDetails(movie: Movie) {
    }

    override fun launchUpcomingMovies() {
    }

    override fun launchPopularMovies() {
    }

    override fun launchTopRatedMovies() {
    }

    override fun launchMovieSearch() {
    }
}