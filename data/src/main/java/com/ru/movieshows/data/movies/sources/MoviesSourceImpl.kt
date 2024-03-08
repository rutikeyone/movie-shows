package com.ru.movieshows.data.movies.sources

import com.ru.movieshows.data.movies.api.MoviesApi
import com.ru.movieshows.data.movies.models.MovieDetailsModel
import com.ru.movieshows.data.movies.models.MoviesPaginationModel
import com.ru.movieshows.data.movies.models.ReviewsPaginationModel
import com.ru.movieshows.data.movies.models.VideoModel
import javax.inject.Inject

class MoviesSourceImpl @Inject constructor(
    private val moviesApi: MoviesApi,
): MoviesSource {

    override suspend fun getMovieReviews(
        movieId: String,
        language: String,
        pageIndex: Int,
    ): ReviewsPaginationModel {
        val response = moviesApi.getMovieReviews(
            movieId = movieId,
            language = language,
            page = pageIndex,
        )
        return response.body() ?: throw NullPointerException()
    }

    override suspend fun getVideosById(
        movieId: String,
        language: String,
    ): List<VideoModel> {
        val response = moviesApi.getVideosByMoviesId(movieId, language)
        val body = response.body() ?: throw NullPointerException()
        return body.results
    }

    override suspend fun getSimilarMovies(
        movieId: String,
        language: String,
        pageIndex: Int,
    ): MoviesPaginationModel {
        val response = moviesApi.getSimilarMovies(
            movieId = movieId,
            language = language,
            page = pageIndex,
        )
        return response.body() ?: throw NullPointerException()
    }

    override suspend fun getDiscoverMovies(
        language: String,
        pageIndex: Int,
        withGenresId: String,
    ): MoviesPaginationModel {
        val response = moviesApi.getDiscoverMovies(
            language = language,
            page = pageIndex,
            withGenresId = withGenresId,
        )
        return response.body() ?: throw NullPointerException()
    }

    override suspend fun getMovieDetails(
        movieId: Int,
        language: String,
    ): MovieDetailsModel {
        val response = moviesApi.getMovieDetails(movieId, language)
        return response.body() ?: throw NullPointerException()
    }

    override suspend fun getNowPlayingMovies(
        language: String,
        pageIndex: Int,
    ): MoviesPaginationModel {
        val response = moviesApi.getMoviesNowPlaying(language, pageIndex)
        return response.body() ?: throw NullPointerException()
    }

    override suspend fun getUpcomingMovies(
        language: String,
        pageIndex: Int,
    ): MoviesPaginationModel {
        val response = moviesApi.getUpcomingMovies(language, pageIndex)
        return response.body() ?: throw NullPointerException()
    }

    override suspend fun getPopularMovies(
        language: String,
        pageIndex: Int
    ): MoviesPaginationModel {
        val response = moviesApi.getPopularMovies(language, pageIndex)
        return response.body() ?: throw NullPointerException()
    }

    override suspend fun getTopRatedMovies(
        language: String,
        pageIndex: Int,
    ): MoviesPaginationModel {
        val response = moviesApi.getTopRatedMovies(language, pageIndex)
        return response.body() ?: throw NullPointerException()
    }

    override suspend fun searchMovies(
        language: String,
        pageIndex: Int,
        query: String?,
    ): MoviesPaginationModel {
        val response = moviesApi.searchMovies(
            language = language,
            page = pageIndex,
            query = query,
        )
        return response.body() ?: throw NullPointerException()
    }
}