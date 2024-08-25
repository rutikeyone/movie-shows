package com.ru.movieshows.data.tv_shows


import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ru.movieshows.core.pagination.PageLoader
import com.ru.movieshows.core.pagination.PagePagingSource
import com.ru.movieshows.data.IODispatcher
import com.ru.movieshows.data.TvShowsDataRepository
import com.ru.movieshows.data.movies.models.ReviewModel
import com.ru.movieshows.data.movies.models.ReviewsPaginationModel
import com.ru.movieshows.data.movies.models.VideoModel
import com.ru.movieshows.data.tv_shows.models.EpisodeModel
import com.ru.movieshows.data.tv_shows.models.SeasonModel
import com.ru.movieshows.data.tv_shows.models.TvShowDetailsModel
import com.ru.movieshows.data.tv_shows.models.TvShowModel
import com.ru.movieshows.data.tv_shows.models.TvShowPaginationModel
import com.ru.movieshows.data.tv_shows.room.TvShowSearchDao
import com.ru.movieshows.data.tv_shows.room.TvShowSearchRoomEntity
import com.ru.movieshows.data.tv_shows.sources.TvShowsSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TvShowsDataRepositoryImpl @Inject constructor(
    private val tvShowsSource: TvShowsSource,
    private val tvShowSearchDao: TvShowSearchDao,
    @IODispatcher private val dispatcher: CoroutineDispatcher,
) : TvShowsDataRepository {

    override fun searchPagedMovies(
        language: String,
        query: String?,
    ): Flow<PagingData<TvShowModel>> {

        val loader: PageLoader<List<TvShowModel>> = { pageIndex ->
            val response = withContext(dispatcher) {
                tvShowsSource.searchTvShows(
                    language = language,
                    page = pageIndex,
                    query = query,
                )
            }

            val totalPages = response.totalPages
            val tvShows = response.result
            Pair(tvShows, totalPages)
        }

        return Pager(config = PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = false,
        ), pagingSourceFactory = {
            PagePagingSource(loader)
        }).flow.flowOn(dispatcher)
    }

    override fun getPagedTvShowReviews(
        language: String,
        seriesId: String,
    ): Flow<PagingData<ReviewModel>> {

        val loader: PageLoader<List<ReviewModel>> = { pageIndex ->
            val response = withContext(dispatcher) {
                tvShowsSource.getTvShowReviews(
                    language = language,
                    page = pageIndex,
                    seriesId = seriesId,
                )
            }

            val totalPages = response.totalPages
            val items = response.results
            Pair(items, totalPages)
        }

        return Pager(config = PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = false,
        ), pagingSourceFactory = {
            PagePagingSource(loader)
        }).flow.flowOn(dispatcher)

    }

    override suspend fun getImagesByTvShowId(
        id: String,
    ): List<String>? {
        return withContext(dispatcher) {
            tvShowsSource.getImagesByTvShowId(id)
        }
    }

    override suspend fun getVideosByTvShowId(
        language: String,
        id: String,
    ): List<VideoModel> {
        return withContext(dispatcher) {
            tvShowsSource.getVideosByTvShowId(
                id = id,
                language = language,
            )
        }
    }

    override suspend fun getSeason(
        language: String,
        seriesId: String,
        seasonNumber: String,
    ): SeasonModel {
        return withContext(dispatcher) {
            tvShowsSource.getSeason(
                language = language,
                seriesId = seriesId,
                seasonNumber = seasonNumber,
            )
        }
    }

    override suspend fun getVideosBySeasonNumber(
        language: String,
        seriesId: String,
        seasonNumber: String
    ): List<VideoModel> {
        return withContext(dispatcher) {
            tvShowsSource.getVideosBySeasonNumber(
                language = language,
                seriesId = seriesId,
                seasonNumber = seasonNumber,
            )
        }
    }

    override suspend fun getImagesBySeasonNumber(
        language: String,
        seriesId: String,
        seasonNumber: String
    ): List<String>? {
        return withContext(dispatcher) {
            tvShowsSource.getImagesBySeasonNumber(
                language = language,
                seriesId = seriesId,
                seasonNumber = seasonNumber,
            )
        }
    }

    override suspend fun getSimilarTvShows(
        language: String,
        page: Int,
        id: String,
    ): TvShowPaginationModel {
        return withContext(dispatcher) {
            tvShowsSource.getSimilarTvShows(
                id = id,
                language = language,
                page = page,
            )
        }
    }

    override suspend fun getRecommendationsTvShows(
        language: String,
        page: Int,
        id: String
    ): TvShowPaginationModel {
        return withContext(dispatcher) {
            tvShowsSource.getRecommendationsTvShows(
                id = id,
                language = language,
                page = page,
            )
        }
    }

    override suspend fun getDiscoverTvShows(
        language: String,
        page: Int,
    ): TvShowPaginationModel {
        return withContext(dispatcher) {
            tvShowsSource.getDiscoverTvShows(
                language = language,
                page = page,
            )
        }
    }

    override suspend fun getTvShowDetails(
        language: String,
        id: String,
    ): TvShowDetailsModel {
        return withContext(dispatcher) {
            tvShowsSource.getTvShowDetails(
                id = id,
                language = language,
            )
        }
    }

    override suspend fun getTopRatedTvShows(
        language: String,
        page: Int,
    ): TvShowPaginationModel {
        return withContext(dispatcher) {
            tvShowsSource.getTopRatedTvShows(
                language = language,
                page = page,
            )
        }
    }

    override suspend fun getPopularTvShows(
        language: String,
        page: Int,
    ): TvShowPaginationModel {
        return withContext(dispatcher) {
            tvShowsSource.getPopularTvShows(
                language = language,
                page = page,
            )
        }
    }

    override suspend fun getOnTheAirTvShows(
        language: String,
        page: Int,
    ): TvShowPaginationModel {
        return withContext(dispatcher) {
            tvShowsSource.getOnTheAirTvShows(
                language = language,
                page = page,
            )
        }
    }

    override fun getPagedTheAirTvShows(
        language: String,
    ): Flow<PagingData<TvShowModel>> {

        val loader: PageLoader<List<TvShowModel>> = { pageIndex ->
            val response = withContext(dispatcher) {
                tvShowsSource.getOnTheAirTvShows(
                    language = language,
                    page = pageIndex,
                )
            }

            val totalPages = response.totalPages
            val items = response.result
            Pair(items, totalPages)
        }

        return Pager(config = PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = false,
        ), pagingSourceFactory = {
            PagePagingSource(loader)
        }).flow.flowOn(dispatcher)

    }

    override fun getPagedTopRatedTvShows(
        language: String,
    ): Flow<PagingData<TvShowModel>> {

        val loader: PageLoader<List<TvShowModel>> = { pageIndex ->
            val response = withContext(dispatcher) {
                tvShowsSource.getTopRatedTvShows(
                    language = language,
                    page = pageIndex,
                )
            }

            val totalPages = response.totalPages
            val items = response.result
            Pair(items, totalPages)
        }

        return Pager(config = PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = false,
        ), pagingSourceFactory = {
            PagePagingSource(loader)
        }).flow.flowOn(dispatcher)

    }

    override fun getPagedPopularTvShows(
        language: String,
    ): Flow<PagingData<TvShowModel>> {

        val loader: PageLoader<List<TvShowModel>> = { pageIndex ->
            val response = withContext(dispatcher) {
                tvShowsSource.getPopularTvShows(
                    language = language,
                    page = pageIndex,
                )
            }

            val totalPages = response.totalPages
            val items = response.result
            Pair(items, totalPages)
        }

        return Pager(config = PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = false,
        ), pagingSourceFactory = {
            PagePagingSource(loader)
        }).flow.flowOn(dispatcher)

    }

    override suspend fun getTrendingTvShows(
        language: String,
        pageIndex: Int,
    ): TvShowPaginationModel {
        return withContext(dispatcher) {
            tvShowsSource.getTrendingTvShows(
                language = language,
                page = pageIndex,
            )
        }
    }

    override suspend fun getEpisodeByNumber(
        language: String,
        seriesId: String,
        seasonNumber: String,
        episodeNumber: Int,
    ): EpisodeModel {
        return withContext(dispatcher) {
            tvShowsSource.getEpisodeByNumber(
                language = language,
                seriesId = seriesId,
                seasonNumber = seasonNumber,
                episodeNumber = episodeNumber,
            )
        }
    }

    override suspend fun getTvReviews(
        language: String,
        seriesId: String,
        pageIndex: Int,
    ): ReviewsPaginationModel {
        return withContext(dispatcher) {
            tvShowsSource.getTvShowReviews(
                seriesId = seriesId,
                language = language,
                page = pageIndex,
            )
        }
    }

    override suspend fun insertTvShowSearch(
        tvShowModel: TvShowModel,
        locale: String,
    ) {
        val tvShowSearch = TvShowSearchRoomEntity(tvShowModel, locale)

        return withContext(dispatcher) {
            tvShowSearchDao.insertTvShowsSearch(tvShowSearch)
        }
    }

    override suspend fun deleteTvShowSearch(
        id: Long,
    ) {
        return withContext(dispatcher) {
            tvShowSearchDao.deleteTvShowSearch(id)
        }
    }

    override fun getAllTvShowsSearch(
        locale: String,
    ): Flow<List<TvShowSearchRoomEntity>> {
        return tvShowSearchDao.getAllTvShowsSearch(locale).flowOn(dispatcher)
    }

    override suspend fun getVideosByEpisodeId(
        language: String, seriesId: String, seasonNumber: String, episodeNumber: Int
    ): List<VideoModel> {
        return withContext(dispatcher) {
            tvShowsSource.getVideosByEpisodeId(
                language = language,
                seriesId = seriesId,
                seasonNumber = seasonNumber,
                episodeNumber = episodeNumber,
            )
        }
    }

    override suspend fun getImagesByEpisodeId(
        seriesId: String, seasonNumber: String, episodeNumber: Int
    ): List<String>? {
        return withContext(dispatcher) {
            tvShowsSource.getImagesByEpisodeId(
                seriesId = seriesId,
                seasonNumber = seasonNumber,
                episodeNumber = episodeNumber,
            )
        }
    }

    companion object {
        private const val PAGE_SIZE = 20
    }

}