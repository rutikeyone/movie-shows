package com.ru.movieshows.movies.presentation.adapters

import com.ru.movieshows.movies.domain.entities.MovieSearch

interface MovieSearchListener {

    fun onDeleteMovieSearch(movieSearch: MovieSearch)

    fun onSelectMovieSearch(movieSearch: MovieSearch)

}