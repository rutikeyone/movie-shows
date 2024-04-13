package com.ru.movieshows.data.tv_shows.sources

import com.ru.movieshows.data.movies.models.ReviewsPaginationModel
import com.ru.movieshows.data.movies.models.VideoModel
import com.ru.movieshows.data.tv_shows.models.EpisodeModel
import com.ru.movieshows.data.tv_shows.models.SeasonModel
import com.ru.movieshows.data.tv_shows.models.TvShowDetailsModel
import com.ru.movieshows.data.tv_shows.models.TvShowPaginationModel

interface TvShowsSource {

    suspend fun getVideosById(
        seriesId: String,
        language: String,
    ): List<VideoModel>

    suspend fun getSeason(
        seriesId: String,
        seasonNumber: String,
        language: String,
    ): SeasonModel

    suspend fun getSimilarTvShows(
        seriesId: String,
        language: String,
        page: Int,
    ) : TvShowPaginationModel

    suspend fun getDiscoverTvShows(
        language: String,
        page: Int,
    ): TvShowPaginationModel

    suspend fun getTvShowDetails(
        id: String,
        language: String,
    ): TvShowDetailsModel

    suspend fun getTopRatedTvShows(
        language: String,
        page: Int,
    ): TvShowPaginationModel

    suspend fun getPopularTvShows(
        language: String,
        page: Int,
    ): TvShowPaginationModel

    suspend fun getOnTheAirTvShows(
        language: String,
        page: Int,
    ): TvShowPaginationModel

    suspend fun getTrendingTvShows(
        language: String,
        page: Int,
    ): TvShowPaginationModel

    suspend fun searchTvShows(
        language: String,
        page: Int,
        query: String?,
    ): TvShowPaginationModel

    suspend fun getEpisodeByNumber(
        language: String,
        seriesId: String,
        seasonNumber: String,
        episodeNumber: Int,
    ): EpisodeModel

    suspend fun getTvShowReviews(
        seriesId: String,
        language: String,
        page: Int,
    ): ReviewsPaginationModel

}