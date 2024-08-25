package com.ru.movieshows.data.movies.sources

import com.google.gson.Gson
import com.ru.movieshows.data.BaseRetrofitSource
import com.ru.movieshows.data.movies.services.MoviesService
import com.ru.movieshows.data.movies.models.MovieDetailsModel
import com.ru.movieshows.data.movies.models.MoviesPaginationModel
import com.ru.movieshows.data.movies.models.ReviewsPaginationModel
import com.ru.movieshows.data.movies.models.VideoModel
import javax.inject.Inject

class MoviesSourceImpl @Inject constructor(
    private val moviesService: MoviesService,
    private val gson: Gson,
) : MoviesSource, BaseRetrofitSource(gson) {

    override suspend fun getMovieReviews(
        movieId: String,
        language: String,
        pageIndex: Int,
    ): ReviewsPaginationModel {
        return moviesService.getMovieReviews(
            movieId = movieId,
            language = language,
            page = pageIndex,
        ).awaitResult { it }
    }

    override suspend fun getVideosById(
        movieId: String,
        language: String,
    ): List<VideoModel> {
        return moviesService.getVideosByMoviesId(
            movieId = movieId,
            language = language,
        ).awaitResult { it.results }
    }

    override suspend fun getSimilarMovies(
        movieId: String,
        language: String,
        pageIndex: Int,
    ): MoviesPaginationModel {
        return moviesService.getSimilarMovies(
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
        return moviesService.getDiscoverMovies(
            language = language,
            page = pageIndex,
            withGenresId = withGenresId,
        ).awaitResult { it }
    }

    override suspend fun getMovieDetails(
        movieId: Int,
        language: String,
    ): MovieDetailsModel {
        return moviesService.getMovieDetails(
            movieId = movieId,
            language = language,
        ).awaitResult { it }
    }

    override suspend fun getNowPlayingMovies(
        language: String,
        pageIndex: Int,
    ): MoviesPaginationModel {
        return moviesService.getMoviesNowPlaying(
            language = language,
            page = pageIndex,
        ).awaitResult { it }
    }

    override suspend fun getUpcomingMovies(
        language: String,
        pageIndex: Int,
    ): MoviesPaginationModel {

        return moviesService.getUpcomingMovies(
            language = language,
            page = pageIndex,
        ).awaitResult { it }
    }

    override suspend fun getPopularMovies(
        language: String,
        pageIndex: Int,
    ): MoviesPaginationModel {
        return moviesService.getPopularMovies(
            language = language,
            page = pageIndex,
        ).awaitResult { it }
    }

    override suspend fun getTopRatedMovies(
        language: String,
        pageIndex: Int,
    ): MoviesPaginationModel {
        return moviesService.getTopRatedMovies(
            language = language,
            page = pageIndex,
        ).awaitResult { it }
    }

    override suspend fun searchMovies(
        language: String,
        pageIndex: Int,
        query: String?,
    ): MoviesPaginationModel {
        return moviesService.searchMovies(
            language = language,
            page = pageIndex,
            query = query,
        ).awaitResult { it }
    }
}