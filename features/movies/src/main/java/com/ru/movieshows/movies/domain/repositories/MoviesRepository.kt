package com.ru.movieshows.movies.domain.repositories

import com.ru.movieshows.movies.domain.entities.Movie

interface MoviesRepository {

    suspend fun getNowPlayingMovies(language: String, page: Int): List<Movie>

    suspend fun getTopRatedMovies(language: String, page: Int): List<Movie>

    suspend fun getPopularMovies(language: String, page: Int): List<Movie>

    suspend fun getUpcomingMovies(language: String, page: Int): List<Movie>

    suspend fun getDiscoverMovies(
        language: String,
        page: Int,
        withGenresId: String,
    ): List<Movie>

}