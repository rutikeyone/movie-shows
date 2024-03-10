package com.ru.movieshows.data.tvshows

import androidx.paging.PagingData
import com.ru.movieshows.data.TvShowsDataRepository
import com.ru.movieshows.data.movies.models.ReviewModel
import com.ru.movieshows.data.movies.models.ReviewsPaginationModel
import com.ru.movieshows.data.movies.models.VideoModel
import com.ru.movieshows.data.tvshows.models.EpisodeModel
import com.ru.movieshows.data.tvshows.models.SeasonModel
import com.ru.movieshows.data.tvshows.models.TvShowDetailsModel
import com.ru.movieshows.data.tvshows.models.TvShowModel
import com.ru.movieshows.data.tvshows.models.TvShowPaginationModel
import com.ru.movieshows.data.tvshows.sources.TvShowsSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TvShowsDataRepositoryImpl @Inject constructor(
    private val tvShowsSource: TvShowsSource,
): TvShowsDataRepository {

    override fun searchPagedMovies(
        language: String,
        query: String?,
    ): Flow<PagingData<TvShowModel>> {
        TODO("Not yet implemented")
    }

    override fun getPagedTvReviews(
        language: String,
        seriesId: String,
    ): Flow<PagingData<ReviewModel>> {
        TODO("Not yet implemented")
    }

    override suspend fun getVideosByMovieId(
        language: String,
        seriesId: String,
    ): List<VideoModel> {
        TODO("Not yet implemented")
    }

    override suspend fun getSeason(
        language: String,
        seriesId: String,
        seasonNumber: String,
    ): SeasonModel {
        TODO("Not yet implemented")
    }

    override suspend fun getSimilarTvShows(
        language: String,
        page: Int,
        seriesId: String,
    ): TvShowPaginationModel {
        TODO("Not yet implemented")
    }

    override suspend fun getDiscoverTvShows(language: String, page: Int): TvShowPaginationModel {
        TODO("Not yet implemented")
    }

    override suspend fun getTvShowDetails(language: String, id: String): TvShowDetailsModel {
        TODO("Not yet implemented")
    }

    override suspend fun getTopRatedTvShows(language: String, page: Int): TvShowPaginationModel {
        TODO("Not yet implemented")
    }

    override suspend fun getPopularTvShows(language: String, page: Int): TvShowPaginationModel {
        TODO("Not yet implemented")
    }

    override suspend fun getOnTheAirTvShows(language: String, page: Int): TvShowPaginationModel {
        TODO("Not yet implemented")
    }

    override suspend fun getPagedTheAirTvShows(language: String): Flow<PagingData<TvShowModel>> {
        TODO("Not yet implemented")
    }

    override suspend fun getPagedTheTopRatedTvShows(language: String): Flow<PagingData<TvShowModel>> {
        TODO("Not yet implemented")
    }

    override suspend fun getPagedPopularTvShows(language: String): Flow<PagingData<TvShowModel>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTrendingTvShows(language: String, page: Int): TvShowPaginationModel {
        TODO("Not yet implemented")
    }

    override suspend fun getEpisodeByNumber(
        language: String,
        seriesId: String,
        seasonNumber: String,
        episodeNumber: Int,
    ): EpisodeModel {
        TODO("Not yet implemented")
    }

    override suspend fun getTvReviews(
        language: String,
        seriesId: String,
        page: Int,
    ): ReviewsPaginationModel {
        TODO("Not yet implemented")
    }

}