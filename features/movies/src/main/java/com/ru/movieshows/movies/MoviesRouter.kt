package com.ru.movieshows.movies

import com.ru.movieshows.movies.domain.entities.Movie

interface MoviesRouter {

    fun launchMovieDetails(movie: Movie)

    fun launchUpcomingMovies()

    fun launchPopularMovies()

    fun launchTopRatedMovies()

    fun launchMovieSearch()

}