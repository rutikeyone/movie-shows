package com.ru.movieshows.movies

import com.ru.movieshows.movies.domain.entities.Movie
import com.ru.movieshows.movies.domain.entities.Video

interface MoviesRouter {

    fun launchMovieDetails(id: Int?)

    fun launchUpcomingMovies()

    fun launchPopularMovies()

    fun launchTopRatedMovies()

    fun launchMovieSearch()

    fun launchVideo(key: String)

    fun launchMovieReviews(id: String?)

}