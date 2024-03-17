package com.ru.movieshows.sources.movies

import com.ru.movieshows.sources.movies.models.GetMovieDetailsResponseModels
import com.ru.movieshows.sources.movies.models.GetMoviesResponseModels
import com.ru.movieshows.sources.movies.models.GetReviewsResponseModels
import com.ru.movieshows.sources.movies.models.GetVideosResponseModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApi {

    @GET("movie/{movie_id}/reviews")
    suspend fun getMovieReviews(@Path("movie_id") movieId: String, @Query("language") language: String, @Query("page") page: Int): Response<GetReviewsResponseModels>

    @GET("movie/{movie_id}/videos")
    suspend fun getVideosByMoviesId(@Path("movie_id") movieId: String, @Query("language") language: String): Response<GetVideosResponseModel>

    @GET("movie/{movie_id}/similar")
    suspend fun getSimilarMovies(@Path("movie_id") movieId: String, @Query("language") language: String, @Query("page") page: Int): Response<GetMoviesResponseModels>

    @GET("discover/movie")
    suspend fun getDiscoverMovies(@Query("language") language: String, @Query("page") page: Int, @Query("with_genres") withGenresId: String): Response<GetMoviesResponseModels>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id") movieId: Int, @Query("language") language: String): Response<GetMovieDetailsResponseModels>

    @GET("movie/now_playing")
    suspend fun getMoviesNowPlaying(@Query("language") language: String, @Query("page") page: Int): Response<GetMoviesResponseModels>

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(@Query("language") language: String, @Query("page") page: Int): Response<GetMoviesResponseModels>

    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("language") language: String, @Query("page") page: Int): Response<GetMoviesResponseModels>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(@Query("language") language: String, @Query("page") page: Int): Response<GetMoviesResponseModels>

    @GET("search/movie")
    suspend fun searchMovies(@Query("language") language: String, @Query("page") page: Int, @Query("query") query: String?) : Response<GetMoviesResponseModels>

}