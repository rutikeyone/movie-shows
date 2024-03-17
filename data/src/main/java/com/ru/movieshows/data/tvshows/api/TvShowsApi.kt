package com.ru.movieshows.data.tvshows.api

import com.ru.movieshows.data.movies.models.ReviewsPaginationModel
import com.ru.movieshows.data.movies.models.VideosModel
import com.ru.movieshows.data.tvshows.models.EpisodeModel
import com.ru.movieshows.data.tvshows.models.SeasonModel
import com.ru.movieshows.data.tvshows.models.TvShowPaginationModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TvShowsApi {

    @GET("tv/{series_id}/videos")
    suspend fun getVideosByTvShowId(
        @Path("series_id") seriesId: String,
        @Query("language") language: String,
    ): Response<VideosModel>

    @GET("tv/{series_id}/season/{season_number}")
    suspend fun getSeason(
        @Path("series_id") seriesId: String,
        @Path("season_number") seasonNumber: String,
        @Query("language") language: String,
    ) : SeasonModel

    @GET("tv/{series_id}/similar")
    suspend fun getSimilarTvShows(
        @Path("series_id") seriesId: String,
        @Query("language") language: String,
        @Query("page") page: Int,
    ): Response<TvShowPaginationModel>

    @GET("discover/tv")
    suspend fun getDiscoverTvShows(
        @Query("language") language: String,
        @Query("page") page: Int,
    ): Response<TvShowPaginationModel>

    @GET("tv/{id}")
    suspend fun getTvShowDetails(
        @Path("id") id: String,
        @Query("language") language: String,
    ): Response<TvShowPaginationModel>

    @GET("tv/top_rated")
    suspend fun getTopRatedTvShows(
        @Query("language") language: String,
        @Query("page") page: Int,
    ): Response<TvShowPaginationModel>

    @GET("tv/popular")
    suspend fun getPopularTvShows(
        @Query("language") language: String,
        @Query("page") page: Int,
    ): Response<TvShowPaginationModel>

    @GET("tv/on_the_air")
    suspend fun getOnTheAirTvShows(
        @Query("language") language: String,
        @Query("page") page: Int,
    ): Response<TvShowPaginationModel>

    @GET("trending/tv/day")
    suspend fun getTrendingTvShows(
        @Query("language") language: String,
        @Query("page") page: Int,
    ) : Response<TvShowPaginationModel>

    @GET("search/tv")
    suspend fun searchTvShows(
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("query") query: String?,
    ) : Response<TvShowPaginationModel>

    @GET("tv/{series_id}/season/{season_number}/episode/{episode_number}")
    suspend fun getEpisodeByNumber(
        @Path("series_id") seriesId: String,
        @Path("season_number") seasonNumber: String,
        @Path("episode_number") episodeNumber: String,
        @Query("language") language: String,
    ): EpisodeModel

    @GET("tv/{series_id}/reviews")
    suspend fun getTvReviews(
        @Path("series_id") seriesId: String,
        @Query("language") language: String,
        @Query("page") page: Int,
    ): Response<ReviewsPaginationModel>

}