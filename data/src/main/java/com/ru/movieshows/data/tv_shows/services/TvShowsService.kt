package com.ru.movieshows.data.tv_shows.services

import com.ru.movieshows.data.movies.models.ReviewsPaginationModel
import com.ru.movieshows.data.movies.models.VideosModel
import com.ru.movieshows.data.tv_shows.models.EpisodeModel
import com.ru.movieshows.data.tv_shows.models.SeasonModel
import com.ru.movieshows.data.tv_shows.models.TvShowDetailsModel
import com.ru.movieshows.data.tv_shows.models.TvShowPaginationModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TvShowsService {

    @GET("tv/{series_id}/videos")
    fun getVideosByTvShowId(
        @Path("series_id") seriesId: String,
        @Query("language") language: String,
    ): Call<VideosModel>

    @GET("tv/{series_id}/season/{season_number}")
    fun getSeason(
        @Path("series_id") seriesId: String,
        @Path("season_number") seasonNumber: String,
        @Query("language") language: String,
    ): Call<SeasonModel>

    @GET("tv/{series_id}/similar")
    fun getSimilarTvShows(
        @Path("series_id") seriesId: String,
        @Query("language") language: String,
        @Query("page") page: Int,
    ): Call<TvShowPaginationModel>

    @GET("discover/tv")
    fun getDiscoverTvShows(
        @Query("language") language: String,
        @Query("page") page: Int,
    ): Call<TvShowPaginationModel>

    @GET("tv/{id}")
    fun getTvShowDetails(
        @Path("id") id: String,
        @Query("language") language: String,
    ): Call<TvShowDetailsModel>

    @GET("tv/top_rated")
    fun getTopRatedTvShows(
        @Query("language") language: String,
        @Query("page") page: Int,
    ): Call<TvShowPaginationModel>

    @GET("tv/popular")
    fun getPopularTvShows(
        @Query("language") language: String,
        @Query("page") page: Int,
    ): Call<TvShowPaginationModel>

    @GET("tv/on_the_air")
    fun getOnTheAirTvShows(
        @Query("language") language: String,
        @Query("page") page: Int,
    ): Call<TvShowPaginationModel>

    @GET("trending/tv/day")
    fun getTrendingTvShows(
        @Query("language") language: String,
        @Query("page") page: Int,
    ): Call<TvShowPaginationModel>

    @GET("search/tv")
    fun searchTvShows(
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("query") query: String?,
    ): Call<TvShowPaginationModel>

    @GET("tv/{series_id}/season/{season_number}/episode/{episode_number}")
    fun getEpisodeByNumber(
        @Path("series_id") seriesId: String,
        @Path("season_number") seasonNumber: String,
        @Path("episode_number") episodeNumber: String,
        @Query("language") language: String,
    ): Call<EpisodeModel>

    @GET("tv/{series_id}/reviews")
    fun getTvReviews(
        @Path("series_id") seriesId: String,
        @Query("language") language: String,
        @Query("page") page: Int,
    ): Call<ReviewsPaginationModel>


}