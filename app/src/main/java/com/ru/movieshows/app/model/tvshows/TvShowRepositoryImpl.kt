package com.ru.movieshows.app.model.tvshows

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import arrow.core.Either
import com.ru.movieshows.app.model.AppFailure
import com.ru.movieshows.app.model.PageLoader
import com.ru.movieshows.app.model.PagePagingSource
import com.ru.movieshows.sources.movies.entities.ReviewEntity
import com.ru.movieshows.sources.movies.entities.VideoEntity
import com.ru.movieshows.sources.tvshows.entities.EpisodeEntity
import com.ru.movieshows.sources.tvshows.entities.SeasonEntity
import com.ru.movieshows.sources.tvshows.entities.TvShowDetailsEntity
import com.ru.movieshows.sources.tvshows.entities.TvShowsEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TvShowRepositoryImpl @Inject constructor(
    private val tvShowSource: TvShowsSource,
) : TvShowRepository {

    override fun searchPagedMovies(
        language: String,
        query: String?,
    ): Flow<PagingData<TvShowsEntity>> {
        val loader: PageLoader<List<TvShowsEntity>> = { pageIndex ->
            val response = tvShowSource.searchTvShows(language, pageIndex, query)
            val totalPages = response.first
            val items = response.second
            Pair(items, totalPages)
        }

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PagePagingSource(loader) }
        ).flow
    }

    override fun getPagedTvReviews(
        language: String,
        seriesId: String,
    ): Flow<PagingData<ReviewEntity>> {
        val loader: PageLoader<List<ReviewEntity>> = { pageIndex ->
            val response = tvShowSource.getTvReviews(seriesId, language, pageIndex)
            val totalPages = response.first
            val items = response.second
            Pair(items, totalPages)
        }

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PagePagingSource(loader) }
        ).flow
    }

    override suspend fun getVideosByMovieId(
        language: String,
        seriesId: String,
    ): Either<AppFailure, ArrayList<VideoEntity>> {
        return try {
            val result = tvShowSource.getVideosById(seriesId, language)
            Either.Right(result)
        } catch (e: AppFailure) {
            return Either.Left(e)
        } catch (e: Exception) {
            return Either.Left(AppFailure.Empty)
        }
    }

    override suspend fun getSeason(
        language: String,
        seriesId: String,
        seasonNumber: String,
    ): Either<AppFailure, SeasonEntity> {
        return try {
            val result = tvShowSource.getSeason(seriesId, seasonNumber, language)
            Either.Right(result)
        } catch (e: AppFailure) {
            Either.Left(e)
        } catch (e: Exception) {
            Either.Left(AppFailure.Empty)
        }
    }

    override suspend fun getSimilarTvShows(
        language: String,
        page: Int,
        seriesId: String,
    ): Either<AppFailure, Pair<Int, ArrayList<TvShowsEntity>>> {
        return try {
            val response = tvShowSource.getDiscoverTvShows(language, page)
            val totalPages = response.first
            val items = response.second
            Either.Right(Pair(totalPages, items))
        } catch (e: AppFailure) {
            return Either.Left(e)
        } catch (e: Exception) {
            return Either.Left(AppFailure.Empty)
        }
    }

    override suspend fun getDiscoverTvShows(
        language: String,
        page: Int,
    ): Either<AppFailure, Pair<Int, ArrayList<TvShowsEntity>>> {
        return try {
            val response = tvShowSource.getDiscoverTvShows(language, page)
            val totalPages = response.first
            val items = response.second
            Either.Right(Pair(totalPages, items))
        } catch (e: AppFailure) {
            Either.Left(e)
        } catch (e: Exception) {
            Either.Left(AppFailure.Empty)
        }
    }

    override suspend fun getTvShowDetails(
        language: String,
        id: String,
    ): Either<AppFailure, TvShowDetailsEntity> {
        return try {
            val result = tvShowSource.getTvShowDetails(id, language)
            Either.Right(result)
        } catch (e: AppFailure) {
            return Either.Left(e)
        } catch (e: Exception) {
            return Either.Left(AppFailure.Empty)
        }
    }

    override suspend fun getTopRatedTvShows(
        language: String,
        page: Int,
    ): Either<AppFailure, Pair<Int, ArrayList<TvShowsEntity>>> {
        return try {
            val response = tvShowSource.getTopRatedTvShows(language, page)
            val totalPages = response.first
            val items = response.second
            Either.Right(Pair(totalPages, items))
        } catch (e: AppFailure) {
            Either.Left(e)
        } catch (e: Exception) {
            Either.Left(AppFailure.Empty)
        }
    }

    override suspend fun getPopularTvShows(
        language: String,
        page: Int,
    ): Either<AppFailure, Pair<Int, ArrayList<TvShowsEntity>>> {
        return try {
            val response = tvShowSource.getPopularTvShows(language, page)
            val totalPages = response.first
            val items = response.second
            Either.Right(Pair(totalPages, items))
        } catch (e: AppFailure) {
            Either.Left(e)
        } catch (e: Exception) {
            Either.Left(AppFailure.Empty)
        }
    }

    override suspend fun getOnTheAirTvShows(
        language: String,
        page: Int,
    ): Either<AppFailure, Pair<Int, ArrayList<TvShowsEntity>>> {
        return try {
            val response = tvShowSource.getOnTheAirTvShows(language, page)
            val totalPages = response.first
            val items = response.second
            Either.Right(Pair(totalPages, items))
        } catch (e: AppFailure) {
            return Either.Left(e)
        } catch (e: Exception) {
            return Either.Left(AppFailure.Empty)
        }
    }

    override suspend fun getPagedTheAirTvShows(language: String): Flow<PagingData<TvShowsEntity>> {
        val loader: PageLoader<List<TvShowsEntity>> = { pageIndex ->
            val response = tvShowSource.getOnTheAirTvShows(language, pageIndex)
            val totalPages = response.first
            val items = response.second
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

    override suspend fun getPagedTheTopRatedTvShows(language: String): Flow<PagingData<TvShowsEntity>> {
        val loader: PageLoader<List<TvShowsEntity>> = { pageIndex ->
            val response = tvShowSource.getTopRatedTvShows(language, pageIndex)
            val totalPages = response.first
            val items = response.second
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
        language: String
    ): Flow<PagingData<TvShowsEntity>> {
        val loader: PageLoader<List<TvShowsEntity>> = { pageIndex ->
            val response = tvShowSource.getPopularTvShows(language, pageIndex)
            val totalPages = response.first
            val items = response.second
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
    ): Either<AppFailure, Pair<Int, ArrayList<TvShowsEntity>>> {
        return try {
            val result = tvShowSource.getTrendingTvShows(language, page)
            val totalPages = result.first
            val items = result.second
            Either.Right(Pair(totalPages, items))
        } catch (e: AppFailure) {
            return Either.Left(e)
        } catch (e: Exception) {
            return Either.Left(AppFailure.Empty)
        }
    }

    override suspend fun getEpisodeByNumber(
        language: String,
        seriesId: String,
        seasonNumber: String,
        episodeNumber: Int,
    ): Either<AppFailure, EpisodeEntity> {
        return try {
            val result = tvShowSource.getEpisodeByNumber(language, seriesId, seasonNumber, episodeNumber)
            Either.Right(result)
        } catch (e: AppFailure) {
            return Either.Left(e)
        } catch (e: Exception) {
            return Either.Left(AppFailure.Empty)
        }
    }

    override suspend fun getTvReviews(
        language: String,
        seriesId: String,
        page: Int,
    ): Either<AppFailure, Pair<Int, ArrayList<ReviewEntity>>> {
        return try {
            val result = tvShowSource.getTvReviews(seriesId, language, page)
            val totalPages = result.first
            val items = result.second
            Either.Right(Pair(totalPages, items))
        } catch (e: AppFailure) {
            return Either.Left(e)
        } catch (e: Exception) {
            return Either.Left(AppFailure.Empty)
        }
    }

    private companion object {
        const val PAGE_SIZE = 20
    }
}