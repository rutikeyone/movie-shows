package com.ru.movieshows.data.movies.api

import com.ru.movieshows.data.movies.models.MovieDetailsModel
import com.ru.movieshows.data.movies.models.MoviesPaginationModel
import com.ru.movieshows.data.movies.models.ReviewsPaginationModel
import com.ru.movieshows.data.movies.models.VideosModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApi {

    @GET("movie/{movie_id}/reviews")
    fun getMovieReviews(
        @Path("movie_id") movieId: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Call<ReviewsPaginationModel>

    @GET("movie/{movie_id}/videos")
    fun getVideosByMoviesId(
        @Path("movie_id") movieId: String,
        @Query("language") language: String
    ): Call<VideosModel>

    @GET("movie/{movie_id}/similar")
    fun getSimilarMovies(
        @Path("movie_id") movieId: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Call<MoviesPaginationModel>

    @GET("discover/movie")
    fun getDiscoverMovies(
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("with_genres") withGenresId: String
    ): Call<MoviesPaginationModel>

    @GET("movie/{movie_id}")
    fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String
    ): Call<MovieDetailsModel>

    @GET("movie/now_playing")
    fun getMoviesNowPlaying(
        @Query("language") language: String,
        @Query("page") page: Int
    ): Call<MoviesPaginationModel>

    @GET("movie/upcoming")
    fun getUpcomingMovies(
        @Query("language") language: String,
        @Query("page") page: Int
    ): Call<MoviesPaginationModel>

    @GET("movie/popular")
    fun getPopularMovies(
        @Query("language") language: String,
        @Query("page") page: Int
    ): Call<MoviesPaginationModel>

    @GET("movie/top_rated")
    fun getTopRatedMovies(
        @Query("language") language: String,
        @Query("page") page: Int
    ): Call<MoviesPaginationModel>

    @GET("search/movie")
    fun searchMovies(
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("query") query: String?
    ) : Call<MoviesPaginationModel>

}