package com.ru.movieshows.data.movies.sources

import com.google.gson.Gson
import com.ru.movieshows.data.BaseRetrofitSource
import com.ru.movieshows.data.movies.api.MoviesApi
import com.ru.movieshows.data.movies.models.MovieDetailsModel
import com.ru.movieshows.data.movies.models.MoviesPaginationModel
import com.ru.movieshows.data.movies.models.ReviewsPaginationModel
import com.ru.movieshows.data.movies.models.VideoModel
import javax.inject.Inject

class MoviesSourceImpl @Inject constructor(
    private val moviesApi: MoviesApi,
    private val gson: Gson,
) : MoviesSource, BaseRetrofitSource(gson) {

    override suspend fun getMovieReviews(
        movieId: String,
        language: String,
        pageIndex: Int,
    ): ReviewsPaginationModel {
        return moviesApi.getMovieReviews(
            movieId = movieId,
            language = language,
            page = pageIndex,
        ).awaitResult { it }
    }

    override suspend fun getVideosById(
        movieId: String,
        language: String,
    ): List<VideoModel> {
        return moviesApi.getVideosByMoviesId(
            movieId = movieId,
            language = language,
        ).awaitResult { it.results }
    }

    override suspend fun getSimilarMovies(
        movieId: String,
        language: String,
        pageIndex: Int,
    ): MoviesPaginationModel {
        return moviesApi.getSimilarMovies(
            movieId = movieId,
            language = language,
            page = pageIndex,
        ).awaitResult { it }
    }

    override suspend fun getDiscoverMovies(
        language: String,
        pageIndex: Int,
        withGenresId: String,
    ): MoviesPaginationModel {
        return moviesApi.getDiscoverMovies(
            language = language,
            page = pageIndex,
            withGenresId = withGenresId,
        ).awaitResult { it }
    }

    override suspend fun getMovieDetails(
        movieId: Int,
        language: String,
    ): MovieDetailsModel {
        return moviesApi.getMovieDetails(
            movieId = movieId,
            language = language,
        ).awaitResult { it }
    }

    override suspend fun getNowPlayingMovies(
        language: String,
        pageIndex: Int,
    ): MoviesPaginationModel {
        return moviesApi.getMoviesNowPlaying(
            language = language,
            page = pageIndex,
        ).awaitResult { it }
    }

    override suspend fun getUpcomingMovies(
        language: String,
        pageIndex: Int,
    ): MoviesPaginationModel {
        return moviesApi.getUpcomingMovies(
            language = language,
            page = pageIndex,
        ).awaitResult { it }
    }

    override suspend fun getPopularMovies(
        language: String,
        pageIndex: Int,
    ): MoviesPaginationModel {
        return moviesApi.getPopularMovies(
            language = language,
            page = pageIndex,
        ).awaitResult { it }
    }

    override suspend fun getTopRatedMovies(
        language: String,
        pageIndex: Int,
    ): MoviesPaginationModel {
        return moviesApi.getTopRatedMovies(
            language = language,
            page = pageIndex,
        ).awaitResult { it }
    }

    override suspend fun searchMovies(
        language: String,
        pageIndex: Int,
        query: String?,
    ): MoviesPaginationModel {
        return moviesApi.searchMovies(
            language = language,
            page = pageIndex,
            query = query,
        ).awaitResult { it }
    }
}