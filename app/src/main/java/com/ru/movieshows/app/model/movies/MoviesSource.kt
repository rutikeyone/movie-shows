package com.ru.movieshows.app.model.movies

import com.ru.movieshows.sources.movies.entities.MovieDetailsEntity
import com.ru.movieshows.sources.movies.entities.MovieEntity
import com.ru.movieshows.sources.movies.entities.ReviewEntity
import com.ru.movieshows.sources.movies.entities.VideoEntity

interface MoviesSource {

    suspend fun getMovieReviews(movieId: String, language: String, page: Int): Pair<Int, ArrayList<ReviewEntity>>

    suspend fun getVideosById(movieId: String, language: String): ArrayList<VideoEntity>

    suspend fun getSimilarMovies(movieId: String, language: String, page: Int): ArrayList<MovieEntity>

    suspend fun getDiscoverMovies(language: String, page: Int, withGenresId: String): ArrayList<MovieEntity>

    suspend fun getMovieDetails(movieId: Int, language: String): MovieDetailsEntity

    suspend fun getNowPlayingMovies(language: String, page: Int): ArrayList<MovieEntity>

    suspend fun getUpcomingMovies(language: String, page: Int): Pair<Int, ArrayList<MovieEntity>>

    suspend fun getPopularMovies(language: String, page: Int): Pair<Int, ArrayList<MovieEntity>>

    suspend fun getTopRatedMovies(language: String, page: Int): Pair<Int, ArrayList<MovieEntity>>

    suspend fun searchMovies(language: String, page: Int, query: String?): Pair<Int, ArrayList<MovieEntity>>

}