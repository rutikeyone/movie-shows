package com.ru.movieshows.domain.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ru.movieshows.R
import com.ru.movieshows.data.dto.TvShowDto
import com.ru.movieshows.data.repository.TvShowRepository
import com.ru.movieshows.domain.entity.SeasonEntity
import com.ru.movieshows.domain.entity.TvShowDetailsEntity
import com.ru.movieshows.domain.entity.TvShowsEntity
import com.ru.movieshows.domain.utils.AppFailure
import com.ru.movieshows.presentation.screens.movie_reviews.PageLoader
import com.ru.movieshows.presentation.screens.movie_reviews.PagingSource
import kotlinx.coroutines.flow.Flow
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.Locale
import javax.inject.Inject

class TvShowsRepositoryImpl @Inject constructor(private val tvShowsDto: TvShowDto): TvShowRepository {

    override suspend fun getSeason(language: String, seriesId: String, seasonNumber: String): Result<SeasonEntity> {
        return try {
            val seasonModel = tvShowsDto.getSeason(seriesId, seasonNumber, language)
            val seasonEntity = seasonModel.toEntity()
            return Result.success(seasonEntity)
        } catch (e: SocketTimeoutException) {
            return Result.failure(AppFailure.Connection)
        }
        catch (e: UnknownHostException) {
            return Result.failure(AppFailure.Connection)
        }
        catch (e: ConnectException) {
            val tag = Locale.getDefault().toLanguageTag()
            if(tag == AccountRepositoryImpl.russiaLanguageTag) {
                val failure = AppFailure.Message(R.string.problems_may_arise_when_connecting_from_russia_use_a_vpn_service)
                return Result.failure(failure)
            }
            return Result.failure(AppFailure.Connection)
        }
        catch (e: Exception) {
            return Result.failure(AppFailure.Pure)
        }
    }

    override suspend fun getSimilarTvShows(language: String, page: Int, seriesId: String): Result<ArrayList<TvShowsEntity>> {
        return try {
            val getSimilarTvShowsResponse = tvShowsDto.getSimilarTvShows(seriesId, language, page)
            return if(getSimilarTvShowsResponse.isSuccessful && getSimilarTvShowsResponse.body() != null){
                val tvShowModels = getSimilarTvShowsResponse.body()!!.result
                val tvShowEntities = tvShowModels.map { it.toEntity() }
                val results = ArrayList(tvShowEntities)
                Result.success(results)
            } else {
                val exception = AppFailure.Pure
                Result.failure(exception)
            }
        }
        catch (e: ConnectException) {
            val movieException = AppFailure.Connection
            Result.failure(movieException)
        }
        catch (e: Exception) {
            val exception = AppFailure.Pure
            exception.initCause(e)
            Result.failure(exception)
        }
    }

    override suspend fun getDiscoverTvShows(
        language: String,
        page: Int,
    ): Result<ArrayList<TvShowsEntity>> {
        return try {
            val getDiscoverTvShowsResponse = tvShowsDto.getDiscoverTvShows(language, page)
            return if(getDiscoverTvShowsResponse.isSuccessful && getDiscoverTvShowsResponse.body() != null){
                val tvShowModes = getDiscoverTvShowsResponse.body()!!.result
                val tvShowsEntities = tvShowModes.map { it.toEntity() }
                Result.success(ArrayList(tvShowsEntities))
            } else {
                val exception = AppFailure.Pure
                Result.failure(exception)
            }
        }
        catch (e: ConnectException) {
            val movieException = AppFailure.Connection
            Result.failure(movieException)
        }
        catch (e: Exception) {
            val exception = AppFailure.Pure
            exception.initCause(e)
            Result.failure(exception)
        }
    }

    override suspend fun getTvShowDetails(
        language: String,
        id: String,
    ): Result<TvShowDetailsEntity> {
        return try {
            val getTvShowDetailsResponse = tvShowsDto.getTvShowDetails(id, language)
            return if(getTvShowDetailsResponse.isSuccessful && getTvShowDetailsResponse.body() != null) {
                val tvShowDetailsModel = getTvShowDetailsResponse.body()!!
                val tvShowDetailsEntity = tvShowDetailsModel.toEntity()
                return Result.success(tvShowDetailsEntity)
            } else {
                val failure = AppFailure.Pure
                Result.failure(failure)
            }
        }
        catch (e: ConnectException) {
            val movieException = AppFailure.Connection
            Result.failure(movieException)
        }
        catch (e: Exception) {
            val failure = AppFailure.Pure
            Result.failure(failure)
        }
    }

    override suspend fun getTopRatedTvShows(
        language: String,
        page: Int,
    ): Result<ArrayList<TvShowsEntity>> {
        return try {
            val getTopRatedTvShows = tvShowsDto.getTopRatedTvShows(language, page)
            return if(getTopRatedTvShows.isSuccessful && getTopRatedTvShows.body() != null) {
                val tvShowModels = getTopRatedTvShows.body()!!.result
                val tvShowEntities = tvShowModels.map { it.toEntity() }
                Result.success(ArrayList(tvShowEntities))
            } else {
                val exception = AppFailure.Pure
                Result.failure(exception)
            }
        }
        catch (e: ConnectException) {
            val movieException = AppFailure.Connection
            Result.failure(movieException)
        }
        catch (e: Exception) {
            val exception = AppFailure.Pure
            exception.initCause(e)
            Result.failure(exception)
        }
    }

    override suspend fun getPopularTvShows(
        language: String,
        page: Int,
    ): Result<ArrayList<TvShowsEntity>> {
        return try {
            val getPopularTvShowsResponse = tvShowsDto.getPopularTvShows(language, page)
            return if(getPopularTvShowsResponse.isSuccessful && getPopularTvShowsResponse.body() != null) {
                val tvShowModels = getPopularTvShowsResponse.body()!!.result
                val tvShowEntities = tvShowModels.map { it.toEntity() }
                Result.success(ArrayList(tvShowEntities))
            } else {
                val exception = AppFailure.Pure
                Result.failure(exception)
            }
        }
        catch (e: ConnectException) {
            val movieException = AppFailure.Connection
            Result.failure(movieException)
        }
        catch (e: Exception) {
            val exception = AppFailure.Pure
            exception.initCause(e)
            Result.failure(exception)
        }
    }

    override suspend fun getOnTheAirTvShows(
        language: String,
        page: Int,
    ): Result<ArrayList<TvShowsEntity>> {
        return try {
            val getOnTheAirTvShowsResponse = tvShowsDto.getOnTheAirTvShows(language, page)
            return if(getOnTheAirTvShowsResponse.isSuccessful && getOnTheAirTvShowsResponse.body() != null) {
                val tvShowModels = getOnTheAirTvShowsResponse.body()!!.result
                val tvShowEntities = tvShowModels.map { it.toEntity() }
                Result.success(ArrayList(tvShowEntities))
            } else {
                val exception = AppFailure.Pure
                Result.failure(exception)
            }
        }
        catch (e: ConnectException) {
            val movieException = AppFailure.Connection
            Result.failure(movieException)
        }
        catch (e: Exception) {
            val exception = AppFailure.Pure
            exception.initCause(e)
            Result.failure(exception)
        }
    }

    override suspend fun getPagedTheAirTvShows(
        language: String
    ): Flow<PagingData<TvShowsEntity>> {
        val loader: PageLoader<List<TvShowsEntity>> = { pageIndex ->
            val response = tvShowsDto.getOnTheAirTvShows(language, pageIndex)
            val isSuccessful = response.isSuccessful
            val body = response.body()
            if(!isSuccessful || body == null)  {
                throw IllegalStateException("Response must be successful")
            }
            val result = body.result
            val totalPages = body.totalPages
            val tvShowsResult = result.map { it.toEntity() }
            Pair(tvShowsResult.toList(), totalPages)
        }

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = { PagingSource(loader, PAGE_SIZE) }
        ).flow
    }

    override suspend fun getPagedTheTopRatedTvShows(
        language: String
    ): Flow<PagingData<TvShowsEntity>> {
        val loader: PageLoader<List<TvShowsEntity>> = { pageIndex ->
            val response = tvShowsDto.getTopRatedTvShows(language, pageIndex)
            val isSuccessful = response.isSuccessful
            val body = response.body()
            if(!isSuccessful || body == null)  {
                throw IllegalStateException("Response must be successful")
            }
            val result = body.result
            val totalPages = body.totalPages
            val tvShowsResult = result.map { it.toEntity() }
            Pair(tvShowsResult.toList(), totalPages)
        }

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = { PagingSource(loader, PAGE_SIZE) }
        ).flow
    }

    override suspend fun getPagedPopularTvShows(language: String): Flow<PagingData<TvShowsEntity>> {
        val loader: PageLoader<List<TvShowsEntity>> = { pageIndex ->
            val response = tvShowsDto.getPopularTvShows(language, pageIndex)
            val isSuccessful = response.isSuccessful
            val body = response.body()
            if(!isSuccessful || body == null)  {
                throw IllegalStateException("Response must be successful")
            }
            val result = body.result
            val totalPages = body.totalPages
            val tvShowsResult = result.map { it.toEntity() }
            Pair(tvShowsResult.toList(), totalPages)
        }

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = { PagingSource(loader, PAGE_SIZE) }
        ).flow
    }

    override suspend fun getTrendingTvShows(
        language: String,
        page: Int,
    ): Result<ArrayList<TvShowsEntity>> {
        return try {
            val getTrendingTvShowsResponse = tvShowsDto.getTrendingTbShows(language, page)
            val isSuccessful = getTrendingTvShowsResponse.isSuccessful
            val body = getTrendingTvShowsResponse.body()
            return if(isSuccessful && body != null) {
              val tvShowModels = body.result
              val tvShowEntities = tvShowModels.map { it.toEntity() }
              Result.success(ArrayList(tvShowEntities))
            } else {
                val failure = AppFailure.Pure
                Result.failure(failure)
            }
        }
        catch (e: ConnectException) {
            val movieException = AppFailure.Connection
            Result.failure(movieException)
        }
        catch (e: Exception) {
            val failure = AppFailure.Pure
            Result.failure(failure)
        }
    }

    override fun searchPagedMovies(
        language: String,
        query: String?,
    ): Flow<PagingData<TvShowsEntity>> {
       val loader: PageLoader<List<TvShowsEntity>> = { pageIndex ->
           val response = tvShowsDto.searchTvShows(language, pageIndex, query)
           val isSuccessful = response.isSuccessful
           val body = response.body()
           if(!isSuccessful || body == null) {
               throw IllegalStateException("Response must be successful")
           }
           val result = body.result
           val totalPages = body.totalPages
           val tvShowEntities = result.map { it.toEntity() }
           Pair(tvShowEntities.toList(), totalPages)
       }
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PagingSource(loader, PAGE_SIZE) }
        ).flow
    }

    private companion object {
        const val PAGE_SIZE = 10
    }
}