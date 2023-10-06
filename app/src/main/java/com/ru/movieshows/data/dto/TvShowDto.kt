package com.ru.movieshows.data.dto

import com.ru.movieshows.data.model.TvShowsModel
import com.ru.movieshows.data.response.TvShowsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TvShowDto {
    @GET("tv/{series_id}/similar")
    suspend fun getSimilarTvShows(@Path("series_id") seriesId: String, @Query("language") language: String, @Query("page") page: Int): Response<TvShowsResponse>

    @GET("discover/tv")
    suspend fun getDiscoverTvShows(@Query("language") language: String, @Query("page") page: Int): Response<TvShowsResponse>

    @GET("tv/{series_id}")
    suspend fun getTvShowDetails(@Path("series_id") seriesId: String, @Query("language") language: String): Response<TvShowsModel>

    @GET("tv/top_rated")
    suspend fun getTopRatedTvShows(@Query("language") language: String, @Query("page") page: Int): Response<TvShowsResponse>

    @GET("tv/popular")
    suspend fun getPopularTvShows(@Query("language") language: String, @Query("page") page: Int): Response<TvShowsResponse>

    @GET("tv/on_the_air")
    suspend fun getOnTheAirTvShows(@Query("language") language: String, @Query("page") page: Int): Response<TvShowsResponse>

    @GET("trending/tv/day")
    suspend fun getTrendingTbShows(@Query("language") language: String, @Query("page") page: Int) :Response<TvShowsResponse>

    @GET("search/tv")
    suspend fun searchTvShows(@Query("language") language: String, @Query("page") page: Int, @Query("query") query: String?) : Response<TvShowsResponse>
}