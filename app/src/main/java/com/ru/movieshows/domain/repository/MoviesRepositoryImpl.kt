@file:Suppress("UNREACHABLE_CODE")

package com.ru.movieshows.domain.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.gson.Gson
import com.ru.movieshows.data.dto.MoviesDto
import com.ru.movieshows.data.dto.PageLoader
import com.ru.movieshows.data.dto.PagingSource
import com.ru.movieshows.data.repository.MoviesRepository
import com.ru.movieshows.domain.entity.MovieDetailsEntity
import com.ru.movieshows.domain.entity.MovieEntity
import com.ru.movieshows.domain.entity.ReviewEntity
import com.ru.movieshows.domain.entity.VideoEntity
import com.ru.movieshows.domain.utils.AppFailure
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val moviesDto: MoviesDto,
    private val gson: Gson,
): MoviesRepository {

    override fun getPagedTopRatedMovies(language: String): Flow<PagingData<MovieEntity>> {
        val loader: PageLoader<List<MovieEntity>> = { pageIndex ->
            val response = moviesDto.getTopRatedMovies(language, pageIndex)
            val isSuccessful = response.isSuccessful
            val body = response.body()
            if(!isSuccessful || body == null) throw IllegalStateException("Response must be successful")
            val result = body.results
            val totalPages = body.totalPages
            val movieEntities =  result.map { it.toEntity() }
            Pair(movieEntities, totalPages)
        }

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PagingSource(loader, PAGE_SIZE) }
        ).flow
    }

    override fun searchPagedMovies(language: String, query: String?): Flow<PagingData<MovieEntity>> {
        val loader: PageLoader<ArrayList<MovieEntity>> = { pageIndex ->
            val response = moviesDto.searchMovies(language, pageIndex, query)
            if(!response.isSuccessful || response.body() == null) throw IllegalStateException("Response must be successful")
            val body = response.body()!!
            val result = body.results
            val totalPages = body.totalPages
            val movieEntities = ArrayList(result.map { it.toEntity() })
            Pair(movieEntities, totalPages)
        }

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PagingSource(loader, PAGE_SIZE) }
        ).flow
    }

    override fun getPagedMovieReview(
        language: String,
        movieId: String
    ): Flow<PagingData<ReviewEntity>> {
        val loader: PageLoader<List<ReviewEntity>> = { pageIndex ->
            val response = moviesDto.getMovieReviews(movieId, language, pageIndex)
            if(!response.isSuccessful || response.body() == null) throw IllegalStateException("Response must be successful")
            val body = response.body()!!
            val result = body.results
            val totalPages = body.totalPages
            val movieEntities = result.map { it.toEntity() }
            Pair(movieEntities.toList(), totalPages)
        }

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PagingSource(loader, PAGE_SIZE) }
        ).flow
    }

    override fun getPagedPopularMovies(language: String): Flow<PagingData<MovieEntity>> {
        val loader: PageLoader<List<MovieEntity>> = { pageIndex ->
            val response = moviesDto.getPopularMovies(language, pageIndex)
            if(!response.isSuccessful || response.body() == null) throw IllegalStateException("Response must be successful")
            val body = response.body()!!
            val result = body.results
            val totalPages = body.page
            val movieEntities = result.map { it.toEntity() }
            Pair(movieEntities.toList(), totalPages)
        }

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PagingSource(loader, PAGE_SIZE) }
        ).flow
    }

    override fun getPagedUnComingMovies(
        language: String,
    ): Flow<PagingData<MovieEntity>> {
        val loader: PageLoader<List<MovieEntity>> = { pageIndex ->
            val response = moviesDto.getUpcomingMovies(language, pageIndex)
            if(!response.isSuccessful || response.body() == null) throw IllegalStateException("Response must be successful")
            val body = response.body()!!
            val result = body.results
            val totalPages = body.totalPages
            val movieEntities = result.map { it.toEntity() }
            Pair(movieEntities.toList(), totalPages)
        }

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PagingSource(loader, PAGE_SIZE) }
        ).flow
    }

    override suspend fun getMovieReviews(language: String, movieId: String, page: Int): Result<ArrayList<ReviewEntity>> {
        return try {
            val getMovieReviewsResponse = moviesDto.getMovieReviews(movieId, language, page)
            return if(getMovieReviewsResponse.isSuccessful && getMovieReviewsResponse.body() != null) {
                val movieModels = getMovieReviewsResponse.body()!!.results
                val movieEntities = movieModels.map { it.toEntity() }
                Result.success(ArrayList(movieEntities))
            } else {
                val movieException = AppFailure.Pure
                Result.failure(movieException)
            }
        }
        catch (e: Exception) {
            val error = handleError(e, gson)
            return Result.failure(error)
        }
    }

    override suspend fun getVideosByMovieId(
        language: String,
        movieId: String,
    ): Result<ArrayList<VideoEntity>> {
        return try {
            val getVideosByMovieIdResponse = moviesDto.getVideosByMoviesId(movieId, language)
            return if(getVideosByMovieIdResponse.isSuccessful && getVideosByMovieIdResponse.body() != null) {
                val videoModels = getVideosByMovieIdResponse.body()!!.results
                val videoEntities = videoModels.map { it.toEntity() }
                Result.success(ArrayList(videoEntities))
            } else {
                val movieException = AppFailure.Pure
                Result.failure(movieException)
            }
        }
        catch (e: Exception) {
            val error = handleError(e, gson)
            return Result.failure(error)
        }
    }

    override suspend fun getSimilarMovies(language: String, movieId: String, page: Int): Result<ArrayList<MovieEntity>> {
        return try {
            val getSimilarTvShowsResponse = moviesDto.getSimilarMovies(movieId, language, page)
            return if(getSimilarTvShowsResponse.isSuccessful && getSimilarTvShowsResponse.body() != null) {
                val similarMovies = getSimilarTvShowsResponse.body()!!.results
                val similarMovieEntities = similarMovies.map { it.toEntity() }
                Result.success(ArrayList(similarMovieEntities))
            } else {
                val movieException = AppFailure.Pure
                Result.failure(movieException)
            }
        }
        catch (e: Exception) {
            val error = handleError(e, gson)
            return Result.failure(error)
        }
    }

    override suspend fun getDiscoverMovies(
        language: String,
        page: Int,
        withGenresId: String,
    ): Result<ArrayList<MovieEntity>> {
        return try {
            val getDiscoverMoviesResponse = moviesDto.getDiscoverMovies(language, page, withGenresId)
            return if(getDiscoverMoviesResponse.isSuccessful && getDiscoverMoviesResponse.body() != null) {
                val similarTvShowModels = getDiscoverMoviesResponse.body()!!.results
                val similarTvShowEntities = similarTvShowModels.map { it.toEntity() }
                Result.success(ArrayList(similarTvShowEntities))
            } else {
                val movieException = AppFailure.Pure
                Result.failure(movieException)
            }
        }
        catch (e: Exception) {
            val error = handleError(e, gson)
            return Result.failure(error)
        }
    }

    override suspend fun getMovieDetails(
        movieId: Int,
        language: String,
    ): Result<MovieDetailsEntity> {
        return try {
            val getMovieDetailsResponse = moviesDto.getMovieDetails(movieId, language)
            return if(getMovieDetailsResponse.isSuccessful && getMovieDetailsResponse.body() != null) {
                val movieDetailsModels = getMovieDetailsResponse.body()!!
                val movieDetailsEntity = movieDetailsModels.toEntity()
                Result.success(movieDetailsEntity)
            } else {
                val movieException = AppFailure.Pure
                Result.failure(movieException)
            }
        }
        catch (e: Exception) {
            val error = handleError(e, gson)
            return Result.failure(error)
        }
    }

    override suspend fun getMoviesNowPlaying(language: String, page: Int): Result<ArrayList<MovieEntity>> {
        return try {
            val getMovieNowPlayingResponse = moviesDto.getMoviesNowPlaying(language, page)
            return if(getMovieNowPlayingResponse.isSuccessful && getMovieNowPlayingResponse.body() != null) {
                val movieModels = getMovieNowPlayingResponse.body()!!.results
                val movieEntities = movieModels.map { it.toEntity() }
                Result.success(ArrayList(movieEntities))
            } else {
                val movieException = AppFailure.Pure
                Result.failure(movieException)
            }
        }
        catch (e: Exception) {
            val error = handleError(e, gson)
            return Result.failure(error)
        }
    }

    override suspend fun getUpcomingMovies(
        language: String,
        page: Int,
    ): Result<ArrayList<MovieEntity>> {
        return try {
            val getUpcomingMoviesResponse = moviesDto.getUpcomingMovies(language, page)
            return if(getUpcomingMoviesResponse.isSuccessful && getUpcomingMoviesResponse.body() != null) {
                val moviesModels = getUpcomingMoviesResponse.body()!!.results
                val moviesEntities = moviesModels.map { it.toEntity() }
                Result.success(ArrayList(moviesEntities))
            } else {
                val movieException = AppFailure.Pure
                Result.failure(movieException)
            }
        }
        catch (e: Exception) {
            val error = handleError(e, gson)
            return Result.failure(error)
        }
    }

    override suspend fun getPopularMovies(
        language: String,
        page: Int,
    ): Result<ArrayList<MovieEntity>> {
        return try {
            val getPopularMoviesResponse = moviesDto.getPopularMovies(language, page)
            return if(getPopularMoviesResponse.isSuccessful && getPopularMoviesResponse.body() != null) {
                val moviesModels = getPopularMoviesResponse.body()!!.results
                val moviesEntities = moviesModels.map { it.toEntity() }
                Result.success(ArrayList(moviesEntities))
            } else {
                val movieException = AppFailure.Pure
                Result.failure(movieException)
            }
        }
        catch (e: Exception) {
            val error = handleError(e, gson)
            return Result.failure(error)
        }
    }

    override suspend fun getTopRatedMovies(
        language: String,
        page: Int,
    ): Result<ArrayList<MovieEntity>> {
        return try {
            val getTopRatedMovies = moviesDto.getTopRatedMovies(language, page)
            if(getTopRatedMovies.isSuccessful && getTopRatedMovies.body() != null) {
                val topRatedMovieModels = getTopRatedMovies.body()!!.results
                val topRatedMoviesEntity = topRatedMovieModels.map { it.toEntity() }
                Result.success(ArrayList(topRatedMoviesEntity))
            } else {
                val movieException = AppFailure.Pure
                Result.failure(movieException)
            }
        }
        catch (e: Exception) {
            val error = handleError(e, gson)
            return Result.failure(error)
        }
    }

    override suspend fun searchMovies(
        language: String,
        page: Int,
        query: String?
    ): Result<ArrayList<MovieEntity>> {
        return try {
            val searchMovies = moviesDto.searchMovies(language, page, query)
            if(searchMovies.isSuccessful && searchMovies.body() != null) {
                val moviesModels = searchMovies.body()!!.results
                val moviesEntity = moviesModels.map { it.toEntity() }
                Result.success(ArrayList(moviesEntity))
            } else {
                val movieException = AppFailure.Pure
                Result.failure(movieException)
            }
        }
        catch (e: Exception) {
            val error = handleError(e, gson)
            return Result.failure(error)
        }
    }

    private companion object {
        const val PAGE_SIZE = 20
    }
}