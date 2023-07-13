package com.ru.movieshows.data.dto

import com.ru.movieshows.data.response.GetDiscoverMoviesResponse
import com.ru.movieshows.data.response.GetMoviesNowPlayingResponse
import com.ru.movieshows.data.response.GetMovieReviewsResponse
import com.ru.movieshows.data.response.GetSimilarMoviesResponse
import com.ru.movieshows.data.response.GetVideosByMovieIdResponse
import com.ru.movieshows.data.model.MovieDetailsModel
import com.ru.movieshows.data.response.GetPopularMoviesResponse
import com.ru.movieshows.data.response.GetTopRatedMoviesResponse
import com.ru.movieshows.data.response.GetUpcomingMoviesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesDto {
    @GET("movie/{movie_id}/reviews")
    suspend fun getMovieReviews(@Path("movieId") movieId: String, @Query("language") language: String): Response<GetMovieReviewsResponse>

    @GET("movie/{movie_id}/videos")
    suspend fun getVideosByMoviesId(@Path("movie_id") movieId: String, @Query("language") language: String): Response<GetVideosByMovieIdResponse>

    @GET("movie/{movie_id}/similar")
    suspend fun getSimilarMovies(@Path("movie_id") movieId: String, @Query("language") language: String, @Query("page") page: Int): Response<GetSimilarMoviesResponse>

    @GET("discover/movie")
    suspend fun getDiscoverMovies(@Query("language") language: String, @Query("page") page: Int, @Query("with_genres") withGenresId: String): Response<GetDiscoverMoviesResponse>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id") movieId: String, @Query("language") language: String): Response<MovieDetailsModel>

    @GET("movie/now_playing")
    suspend fun getMoviesNowPlaying(@Query("language") language: String, @Query("page") page: Int): Response<GetMoviesNowPlayingResponse>

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(@Query("language") language: String, @Query("page") page: Int): Response<GetUpcomingMoviesResponse>

    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("language") language: String, @Query("page") page: Int): Response<GetPopularMoviesResponse>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(@Query("language") language: String, @Query("page") page: Int): Response<GetTopRatedMoviesResponse>
}
