package com.ru.movieshows.movies

import com.ru.movieshows.movies.domain.entities.Movie
import com.ru.movieshows.movies.domain.entities.Video

interface MoviesRouter {

    fun launchMovieDetails(movie: Movie)

    fun launchUpcomingMovies()

    fun launchPopularMovies()

    fun launchTopRatedMovies()

    fun launchMovieSearch()

    fun launchVideo(video: Video)

    fun launchMovieReviews(id: String?)

}