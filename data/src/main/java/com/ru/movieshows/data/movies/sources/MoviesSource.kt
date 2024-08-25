package com.ru.movieshows.data.movies.sources

import com.ru.movieshows.data.movies.models.MovieDetailsModel
import com.ru.movieshows.data.movies.models.MoviesPaginationModel
import com.ru.movieshows.data.movies.models.ReviewsPaginationModel
import com.ru.movieshows.data.movies.models.VideoModel

interface MoviesSource {

    suspend fun getMovieReviews(
        movieId: String,
        language: String,
        pageIndex: Int,
    ): ReviewsPaginationModel

    suspend fun getVideosById(
        movieId: String,
        language: String,
    ): List<VideoModel>

    suspend fun getSimilarMovies(
        movieId: String,
        language: String,
        pageIndex: Int,
    ): MoviesPaginationModel

    suspend fun getDiscoverMovies(
        language: String,
        pageIndex: Int,
        withGenresId: String,
    ): MoviesPaginationModel

    suspend fun getMovieDetails(
        movieId: Int,
        language: String,
    ): MovieDetailsModel

    suspend fun getNowPlayingMovies(
        language: String,
        pageIndex: Int,
    ): MoviesPaginationModel

    suspend fun getUpcomingMovies(
        language: String,
        pageIndex: Int,
    ): MoviesPaginationModel

    suspend fun getPopularMovies(
        language: String,
        pageIndex: Int,
    ): MoviesPaginationModel

    suspend fun getTopRatedMovies(
        language: String,
        pageIndex: Int,
    ): MoviesPaginationModel

    suspend fun searchMovies(
        language: String,
        pageIndex: Int,
        query: String?
    ): MoviesPaginationModel

}