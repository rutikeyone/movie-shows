package com.ru.movieshows.data.tv_shows.sources

import com.google.gson.Gson
import com.ru.movieshows.data.BaseRetrofitSource
import com.ru.movieshows.data.movies.models.ReviewsPaginationModel
import com.ru.movieshows.data.movies.models.VideoModel
import com.ru.movieshows.data.tv_shows.api.TvShowsApi
import com.ru.movieshows.data.tv_shows.models.EpisodeModel
import com.ru.movieshows.data.tv_shows.models.SeasonModel
import com.ru.movieshows.data.tv_shows.models.TvShowDetailsModel
import com.ru.movieshows.data.tv_shows.models.TvShowPaginationModel
import javax.inject.Inject

class TvShowsSourceImpl @Inject constructor(
    private val tvShowsApi: TvShowsApi,
    private val gson: Gson,
) : TvShowsSource, BaseRetrofitSource(gson) {

    override suspend fun getVideosById(
        seriesId: String,
        language: String,
    ): List<VideoModel> {
        return tvShowsApi.getVideosByTvShowId(
            seriesId = seriesId,
            language = language,
        ).awaitResult { it.results }
    }

    override suspend fun getSeason(
        seriesId: String,
        seasonNumber: String,
        language: String,
    ): SeasonModel {
        return tvShowsApi.getSeason(
            seriesId = seriesId,
            seasonNumber = seasonNumber,
            language = language,
        ).awaitResult { it }
    }

    override suspend fun getSimilarTvShows(
        seriesId: String,
        language: String,
        page: Int,
    ): TvShowPaginationModel {
        return tvShowsApi.getSimilarTvShows(
            seriesId = seriesId,
            language = language,
            page = page,
        ).awaitResult { it }
    }

    override suspend fun getDiscoverTvShows(
        language: String,
        page: Int,
    ): TvShowPaginationModel {
        return tvShowsApi.getDiscoverTvShows(
            language = language,
            page = page,
        ).awaitResult { it }
    }

    override suspend fun getTvShowDetails(
        id: String,
        language: String,
    ): TvShowDetailsModel {
        return tvShowsApi.getTvShowDetails(
            id = id,
            language = language,
        ).awaitResult { it }
    }

    override suspend fun getTopRatedTvShows(
        language: String,
        page: Int,
    ): TvShowPaginationModel {
        return tvShowsApi.getTopRatedTvShows(
            language = language,
            page = page,
        ).awaitResult { it }
    }

    override suspend fun getPopularTvShows(
        language: String,
        page: Int,
    ): TvShowPaginationModel {
        return tvShowsApi.getPopularTvShows(
            language = language,
            page = page,
        ).awaitResult { it }
    }

    override suspend fun getOnTheAirTvShows(
        language: String,
        page: Int,
    ): TvShowPaginationModel {
        return tvShowsApi.getOnTheAirTvShows(
            language = language,
            page = page,
        ).awaitResult { it }
    }

    override suspend fun getTrendingTvShows(
        language: String,
        page: Int,
    ): TvShowPaginationModel {
        return tvShowsApi.getTrendingTvShows(
            language = language,
            page = page,
        ).awaitResult { it }
    }

    override suspend fun searchTvShows(
        language: String,
        page: Int,
        query: String?,
    ): TvShowPaginationModel {
        return tvShowsApi.searchTvShows(
            language = language,
            page = page,
            query = query
        ).awaitResult { it }
    }

    override suspend fun getEpisodeByNumber(
        language: String,
        seriesId: String,
        seasonNumber: String,
        episodeNumber: Int,
    ): EpisodeModel {
        return tvShowsApi.getEpisodeByNumber(
            language = language,
            seriesId = seriesId,
            seasonNumber = seasonNumber,
            episodeNumber = episodeNumber.toString(),
        ).awaitResult { it }
    }

    override suspend fun getTvShowReviews(
        seriesId: String,
        language: String,
        page: Int,
    ): ReviewsPaginationModel {
        return tvShowsApi.getTvReviews(
            seriesId = seriesId,
            language = language,
            page = page,
        ).awaitResult { it }
    }

}