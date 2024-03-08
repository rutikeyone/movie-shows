package com.ru.movieshows.data.movies.api

import com.ru.movieshows.data.movies.models.MovieDetailsModel
import com.ru.movieshows.data.movies.models.MoviesPaginationModel
import com.ru.movieshows.data.movies.models.ReviewsPaginationModel
import com.ru.movieshows.data.movies.models.VideosModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApi {

    @GET("movie/{movie_id}/reviews")
    suspend fun getMovieReviews(
        @Path("movie_id") movieId: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Response<ReviewsPaginationModel>

    @GET("movie/{movie_id}/videos")
    suspend fun getVideosByMoviesId(
        @Path("movie_id") movieId: String,
        @Query("language") language: String
    ): Response<VideosModel>

    @GET("movie/{movie_id}/similar")
    suspend fun getSimilarMovies(
        @Path("movie_id") movieId: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Response<MoviesPaginationModel>

    @GET("discover/movie")
    suspend fun getDiscoverMovies(
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("with_genres") withGenresId: String
    ): Response<MoviesPaginationModel>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String
    ): Response<MovieDetailsModel>

    @GET("movie/now_playing")
    suspend fun getMoviesNowPlaying(
        @Query("language") language: String,
        @Query("page") page: Int
    ): Response<MoviesPaginationModel>

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("language") language: String,
        @Query("page") page: Int
    ): Response<MoviesPaginationModel>

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("language") language: String,
        @Query("page") page: Int
    ): Response<MoviesPaginationModel>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("language") language: String,
        @Query("page") page: Int
    ): Response<MoviesPaginationModel>

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("query") query: String?
    ) : Response<MoviesPaginationModel>

}