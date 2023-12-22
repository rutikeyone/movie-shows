package com.ru.movieshows.presentation.viewmodel.movie_search.state

import com.ru.movieshows.domain.entity.MovieEntity

data class MovieSearchState(
    val language: String,
    val query: String,
    val notPlayingMovies: ArrayList<MovieEntity>?,
    val isSearchMode: Boolean,
) {
   val nowPlayingInPending: Boolean
       get() = notPlayingMovies == null
}