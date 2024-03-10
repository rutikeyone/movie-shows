package com.ru.movieshows.data.tvshows.sources

import com.ru.movieshows.data.movies.models.ReviewsPaginationModel
import com.ru.movieshows.data.movies.models.VideoModel
import com.ru.movieshows.data.tvshows.models.EpisodeModel
import com.ru.movieshows.data.tvshows.models.SeasonModel
import com.ru.movieshows.data.tvshows.models.TvShowDetailsModel
import com.ru.movieshows.data.tvshows.models.TvShowPaginationModel
import javax.inject.Inject

class TvShowsSourceImpl @Inject constructor(
    private val tvShowsSource: TvShowsSource,
): TvShowsSource {

    override suspend fun getVideosById(
        seriesId: String,
        language: String,
    ): List<VideoModel> {
        return tvShowsSource.getVideosById(
            seriesId, language
        )
    }

    override suspend fun getSeason(
        seriesId: String,
        seasonNumber: String,
        language: String,
    ): SeasonModel {
        return tvShowsSource.getSeason(
            seriesId = seriesId,
            seasonNumber = seasonNumber,
            language = language,
        )
    }

    override suspend fun getSimilarTvShows(
        seriesId: String,
        language: String,
        page: Int,
    ): TvShowPaginationModel {
        return tvShowsSource.getSimilarTvShows(
            seriesId = seriesId,
            language = language,
            page = page,
        )
    }

    override suspend fun getDiscoverTvShows(
        language: String,
        page: Int,
    ): TvShowPaginationModel {
        return tvShowsSource.getDiscoverTvShows(
            language, page
        )
    }

    override suspend fun getTvShowDetails(
        id: String,
        language: String,
    ): TvShowDetailsModel {
        return tvShowsSource.getTvShowDetails(
            id, language
        )
    }

    override suspend fun getTopRatedTvShows(
        language: String,
        page: Int,
    ): TvShowPaginationModel {
        return tvShowsSource.getTopRatedTvShows(
            language, page
        )
    }

    override suspend fun getPopularTvShows(
        language: String,
        page: Int,
    ): TvShowPaginationModel {
        return tvShowsSource.getPopularTvShows(
            language, page
        )
    }

    override suspend fun getOnTheAirTvShows(
        language: String,
        page: Int,
    ): TvShowPaginationModel {
        return tvShowsSource.getOnTheAirTvShows(
            language, page
        )
    }

    override suspend fun getTrendingTvShows(
        language: String,
        page: Int,
    ): TvShowPaginationModel {
        return tvShowsSource.getTrendingTvShows(
            language, page
        )
    }

    override suspend fun searchTvShows(
        language: String,
        page: Int,
        query: String?,
    ): TvShowPaginationModel {
        return tvShowsSource.searchTvShows(
            language = language,
            page = page,
            query = query,
        )
    }

    override suspend fun getEpisodeByNumber(
        language: String,
        seriesId: String,
        seasonNumber: String,
        episodeNumber: Int,
    ): EpisodeModel {
        return tvShowsSource.getEpisodeByNumber(
            language = language,
            seriesId = seriesId,
            seasonNumber = seasonNumber,
            episodeNumber = episodeNumber,
        )
    }

    override suspend fun getTvShowReviews(
        seriesId: String,
        language: String,
        page: Int,
    ): ReviewsPaginationModel {
        return tvShowsSource.getTvShowReviews(
            seriesId = seriesId,
            language = language,
            page = page,
        )
    }

}