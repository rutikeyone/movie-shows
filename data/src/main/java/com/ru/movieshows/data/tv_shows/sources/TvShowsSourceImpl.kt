package com.ru.movieshows.data.tv_shows.sources

import com.google.gson.Gson
import com.ru.movieshows.data.BaseRetrofitSource
import com.ru.movieshows.data.movies.models.ReviewsPaginationModel
import com.ru.movieshows.data.movies.models.VideoModel
import com.ru.movieshows.data.tv_shows.services.TvShowsService
import com.ru.movieshows.data.tv_shows.models.EpisodeModel
import com.ru.movieshows.data.tv_shows.models.SeasonModel
import com.ru.movieshows.data.tv_shows.models.TvShowDetailsModel
import com.ru.movieshows.data.tv_shows.models.TvShowPaginationModel
import kotlinx.coroutines.delay
import javax.inject.Inject

class TvShowsSourceImpl @Inject constructor(
    private val tvShowsService: TvShowsService,
    private val gson: Gson,
) : TvShowsSource, BaseRetrofitSource(gson) {

    override suspend fun getVideosById(
        seriesId: String,
        language: String,
    ): List<VideoModel> {
        return tvShowsService.getVideosByTvShowId(
            seriesId = seriesId,
            language = language,
        ).awaitResult { it.results }
    }

    override suspend fun getSeason(
        seriesId: String,
        seasonNumber: String,
        language: String,
    ): SeasonModel {
        return tvShowsService.getSeason(
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
        return tvShowsService.getSimilarTvShows(
            seriesId = seriesId,
            language = language,
            page = page,
        ).awaitResult { it }
    }

    override suspend fun getDiscoverTvShows(
        language: String,
        page: Int,
    ): TvShowPaginationModel {
        return tvShowsService.getDiscoverTvShows(
            language = language,
            page = page,
        ).awaitResult { it }
    }

    override suspend fun getTvShowDetails(
        id: String,
        language: String,
    ): TvShowDetailsModel {
        return tvShowsService.getTvShowDetails(
            id = id,
            language = language,
        ).awaitResult { it }
    }

    override suspend fun getTopRatedTvShows(
        language: String,
        page: Int,
    ): TvShowPaginationModel {
        return tvShowsService.getTopRatedTvShows(
            language = language,
            page = page,
        ).awaitResult { it }
    }

    override suspend fun getPopularTvShows(
        language: String,
        page: Int,
    ): TvShowPaginationModel {
        return tvShowsService.getPopularTvShows(
            language = language,
            page = page,
        ).awaitResult { it }
    }

    override suspend fun getOnTheAirTvShows(
        language: String,
        page: Int,
    ): TvShowPaginationModel {
        return tvShowsService.getOnTheAirTvShows(
            language = language,
            page = page,
        ).awaitResult { it }
    }

    override suspend fun getTrendingTvShows(
        language: String,
        page: Int,
    ): TvShowPaginationModel {
        return tvShowsService.getTrendingTvShows(
            language = language,
            page = page,
        ).awaitResult { it }
    }

    override suspend fun searchTvShows(
        language: String,
        page: Int,
        query: String?,
    ): TvShowPaginationModel {
        return tvShowsService.searchTvShows(
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
        return tvShowsService.getEpisodeByNumber(
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
        return tvShowsService.getTvReviews(
            seriesId = seriesId,
            language = language,
            page = page,
        ).awaitResult { it }
    }

}