package com.ru.movieshows.data.dto

import com.ru.movieshows.data.response.GetDiscoverTvShowsResponse
import com.ru.movieshows.data.response.GetSimilarMoviesResponse
import com.ru.movieshows.data.model.TvShowsModel
import com.ru.movieshows.data.response.GetOnTheAirTvShowsResponse
import com.ru.movieshows.data.response.GetPopularTvShowsResponse
import com.ru.movieshows.data.response.GetTopRatedTvShowsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TvShowDto {
    @GET("tv/{series_id}/similar")
    suspend fun getSimilarTvShows(@Path("series_id") seriesId: String, @Query("language") language: String, @Query("page") page: Int): Response<GetSimilarMoviesResponse>

    @GET("discover/tv")
    suspend fun getDiscoverTvShows(@Query("language") language: String, @Query("page") page: Int): Response<GetDiscoverTvShowsResponse>

    @GET("tv/{series_id}")
    suspend fun getTvShowDetails(@Path("series_id") seriesId: String, @Query("language") language: String): Response<TvShowsModel>

    @GET("tv/top_rated")
    suspend fun getTopRatedTvShows(@Query("language") language: String, @Query("page") page: Int): Response<GetTopRatedTvShowsResponse>

    @GET("tv/popular")
    suspend fun getPopularTvShows(@Query("language") language: String, @Query("page") page: Int): Response<GetPopularTvShowsResponse>

    @GET("tv/on_the_air")
    suspend fun getOnTheAirTvShows(@Query("language") language: String, @Query("page") page: Int): Response<GetOnTheAirTvShowsResponse>
}