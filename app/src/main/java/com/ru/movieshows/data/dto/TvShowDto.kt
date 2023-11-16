package com.ru.movieshows.data.dto

import com.ru.movieshows.data.model.SeasonModel
import com.ru.movieshows.data.model.TvShowDetailsModel
import com.ru.movieshows.data.response.TvShowsResponse
import com.ru.movieshows.data.response.VideosResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TvShowDto {
    @GET("tv/{series_id}/videos")
    suspend fun getVideosByTvShowId(@Path("series_id") seriesId: String, @Query("language") language: String): Response<VideosResponse>

    @GET("tv/{series_id}/season/{season_number}")
    suspend fun getSeason(@Path("series_id") seriesId: String, @Path("season_number") seasonNumber: String, @Query("language") language: String) : SeasonModel

    @GET("tv/{series_id}/similar")
    suspend fun getSimilarTvShows(@Path("series_id") seriesId: String, @Query("language") language: String, @Query("page") page: Int): Response<TvShowsResponse>

    @GET("discover/tv")
    suspend fun getDiscoverTvShows(@Query("language") language: String, @Query("page") page: Int): Response<TvShowsResponse>

    @GET("tv/{id}")
    suspend fun getTvShowDetails(@Path("id") id: String, @Query("language") language: String): Response<TvShowDetailsModel>

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