package com.ru.movieshows.app.presentation.viewmodel.movies.state

import com.ru.movieshows.sources.movies.entities.MovieEntity

data class MovieSearchState(
    val language: String,
    val query: String,
    val notPlayingMovies: ArrayList<MovieEntity>?,
    val isSearchMode: Boolean,
) {
   val nowPlayingInPending: Boolean
       get() = notPlayingMovies == null
}