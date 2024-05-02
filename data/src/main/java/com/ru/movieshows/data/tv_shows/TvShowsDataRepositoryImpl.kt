package com.ru.movieshows.data.tv_shows

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ru.movieshows.core.pagination.PageLoader
import com.ru.movieshows.core.pagination.PagePagingSource
import com.ru.movieshows.data.TvShowsDataRepository
import com.ru.movieshows.data.movies.models.ReviewModel
import com.ru.movieshows.data.movies.models.ReviewsPaginationModel
import com.ru.movieshows.data.movies.models.VideoModel
import com.ru.movieshows.data.tv_shows.models.EpisodeModel
import com.ru.movieshows.data.tv_shows.models.SeasonModel
import com.ru.movieshows.data.tv_shows.models.TvShowDetailsModel
import com.ru.movieshows.data.tv_shows.models.TvShowModel
import com.ru.movieshows.data.tv_shows.models.TvShowPaginationModel
import com.ru.movieshows.data.tv_shows.sources.TvShowsSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TvShowsDataRepositoryImpl @Inject constructor(
    private val tvShowsSource: TvShowsSource,
): TvShowsDataRepository {

    override fun searchPagedMovies(
        language: String,
        query: String?,
    ): Flow<PagingData<TvShowModel>> {

        val loader: PageLoader<List<TvShowModel>> = { pageIndex ->
            val response = tvShowsSource.searchTvShows(
                language = language,
                page = pageIndex,
                query = query,
            )
            val totalPages = response.totalPages
            val tvShows = response.result
            Pair(tvShows, totalPages)
        }

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = { PagePagingSource(loader) }
        ).flow

    }

    override fun getPagedTvReviews(
        language: String,
        seriesId: String,
    ): Flow<PagingData<ReviewModel>> {

        val loader: PageLoader<List<ReviewModel>> = { pageIndex ->
            val response = tvShowsSource.getTvShowReviews(
                language = language,
                page = pageIndex,
                seriesId = seriesId,
            )
            val totalPages = response.totalPages
            val items = response.results
            Pair(items, totalPages)
        }

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = { PagePagingSource(loader) }
        ).flow

    }

    override suspend fun getVideosById(
        language: String,
        seriesId: String,
    ): List<VideoModel> {
        return tvShowsSource.getVideosById(
            language, seriesId,
        )
    }

    override suspend fun getSeason(
        language: String,
        seriesId: String,
        seasonNumber: String,
    ): SeasonModel {
        return tvShowsSource.getSeason(
            language = language,
            seriesId = seriesId,
            seasonNumber = seasonNumber,
        )
    }

    override suspend fun getSimilarTvShows(
        language: String,
        page: Int,
        seriesId: String,
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
        language: String,
        id: String,
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

    override suspend fun getPagedTheAirTvShows(
        language: String,
    ): Flow<PagingData<TvShowModel>> {

        val loader: PageLoader<List<TvShowModel>> = { pageIndex ->
            val response = tvShowsSource.getOnTheAirTvShows(
                language, pageIndex,
            )
            val totalPages = response.totalPages
            val items = response.result
            Pair(items, totalPages)
        }

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = { PagePagingSource(loader) }
        ).flow

    }

    override suspend fun getPagedTheTopRatedTvShows(
        language: String,
    ): Flow<PagingData<TvShowModel>> {

        val loader: PageLoader<List<TvShowModel>> = { pageIndex ->
            val response = tvShowsSource.getTopRatedTvShows(
                language, pageIndex,
            )
            val totalPages = response.totalPages
            val items = response.result
            Pair(items, totalPages)
        }

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = { PagePagingSource(loader) }
        ).flow

    }

    override suspend fun getPagedPopularTvShows(
        language: String,
    ): Flow<PagingData<TvShowModel>> {

        val loader: PageLoader<List<TvShowModel>> = { pageIndex ->
            val response = tvShowsSource.getPopularTvShows(
                language, pageIndex,
            )
            val totalPages = response.totalPages
            val items = response.result
            Pair(items, totalPages)
        }

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = { PagePagingSource(loader) }
        ).flow

    }

    override suspend fun getTrendingTvShows(
        language: String,
        page: Int,
    ): TvShowPaginationModel {
        return tvShowsSource.getTrendingTvShows(
            language, page
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

    override suspend fun getTvReviews(
        language: String,
        seriesId: String,
        page: Int,
    ): ReviewsPaginationModel {
        return tvShowsSource.getTvShowReviews(
            seriesId = seriesId,
            language = language,
            page = page,
        )
    }

    companion object {
        private const val PAGE_SIZE = 20
    }

}