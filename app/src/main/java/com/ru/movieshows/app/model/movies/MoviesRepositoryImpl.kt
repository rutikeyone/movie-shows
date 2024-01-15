package com.ru.movieshows.app.model.movies

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import arrow.core.Either
import com.ru.movieshows.app.model.AppFailure
import com.ru.movieshows.app.model.PageLoader
import com.ru.movieshows.app.model.PagePagingSource
import com.ru.movieshows.sources.movies.entities.MovieDetailsEntity
import com.ru.movieshows.sources.movies.entities.MovieEntity
import com.ru.movieshows.sources.movies.entities.ReviewEntity
import com.ru.movieshows.sources.movies.entities.VideoEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val moviesSource: MoviesSource,
): MoviesRepository {

    override fun searchPagedMovies(
        language: String,
        query: String?,
    ): Flow<PagingData<MovieEntity>> {

        val loader: PageLoader<List<MovieEntity>> = { pageIndex ->
            val result = moviesSource.searchMovies(language, pageIndex, query)
            val totalPages = result.first
            val reviews = result.second
            Pair(reviews, totalPages)
        }

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PagePagingSource(loader) }
        ).flow

    }

    override fun getPagedMovieReview(
        language: String,
        movieId: String,
    ): Flow<PagingData<ReviewEntity>> {

        val loader: PageLoader<List<ReviewEntity>> = { pageIndex ->
            val result = moviesSource.getMovieReviews(movieId, language, pageIndex)
            val totalPages = result.first
            val reviews = result.second
            Pair(reviews, totalPages)
        }

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PagePagingSource(loader) }
        ).flow

    }

    override fun getPagedTopRatedMovies(language: String): Flow<PagingData<MovieEntity>> {

        val loader: PageLoader<List<MovieEntity>> = { pageIndex ->
            val result = moviesSource.getTopRatedMovies(language, pageIndex)
            val totalPages = result.first
            val movies = result.second
            Pair(movies, totalPages)
        }

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PagePagingSource(loader) }
        ).flow

    }

    override fun getPagedPopularMovies(language: String): Flow<PagingData<MovieEntity>> {

        val loader: PageLoader<List<MovieEntity>> = { pageIndex ->
            val result = moviesSource.getPopularMovies(language, pageIndex)
            val totalPages = result.first
            val movies = result.second
            Pair(movies, totalPages)
        }

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PagePagingSource(loader) }
        ).flow

    }

    override fun getPagedUnComingMovies(language: String): Flow<PagingData<MovieEntity>> {

        val loader: PageLoader<List<MovieEntity>> = { pageIndex ->
            val result = moviesSource.getUpcomingMovies(language, pageIndex)
            val totalPages = result.first
            val movies = result.second
            Pair(movies, totalPages)
        }

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PagePagingSource(loader) }
        ).flow

    }

    override suspend fun getMovieReviews(
        language: String,
        movieId: String,
        page: Int,
    ): Either<AppFailure, Pair<Int, ArrayList<ReviewEntity>>> {
        return try {
            val result = moviesSource.getMovieReviews(movieId, language, page)
            Either.Right(result)
        } catch (e: AppFailure) {
            Either.Left(e)
        } catch (e: Exception) {
            Either.Left(AppFailure.Empty)
        }
    }

    override suspend fun getVideosById(
        language: String,
        movieId: String,
    ): Either<AppFailure, ArrayList<VideoEntity>> {
        return try {
            val result = moviesSource.getVideosById(movieId, language)
            Either.Right(result)
        } catch (e: AppFailure) {
            Either.Left(e)
        } catch (e: Exception) {
            Either.Left(AppFailure.Empty)
        }
    }

    override suspend fun getSimilarMovies(
        language: String,
        movieId: String,
        page: Int,
    ): Either<AppFailure, ArrayList<MovieEntity>> {
        return try {
            val result = moviesSource.getSimilarMovies(movieId, language, page)
            Either.Right(result)
        } catch (e: AppFailure) {
            Either.Left(e)
        } catch (e: Exception) {
            Either.Left(AppFailure.Empty)
        }
    }

    override suspend fun getDiscoverMovies(
        language: String,
        page: Int,
        withGenresId: String,
    ): Either<AppFailure, ArrayList<MovieEntity>> {
        return try {
            val result = moviesSource.getDiscoverMovies(language, page, withGenresId)
            Either.Right(result)
        } catch (e: AppFailure) {
            Either.Left(e)
        } catch (e: Exception) {
            Either.Left(AppFailure.Empty)
        }
    }

    override suspend fun getMovieDetails(
        movieId: Int,
        language: String,
    ): Either<AppFailure, MovieDetailsEntity> {
        return try {
            val result = moviesSource.getMovieDetails(movieId, language)
            Either.Right(result)
        } catch (e: AppFailure) {
            Either.Left(e)
        } catch (e: Exception) {
            Either.Left(AppFailure.Empty)
        }
    }

    override suspend fun getNowPlayingMovies(
        language: String,
        page: Int,
    ): Either<AppFailure, ArrayList<MovieEntity>> {
        return try {
            val result = moviesSource.getNowPlayingMovies(language, page)
            Either.Right(result)
        } catch (e: AppFailure) {
            Either.Left(e)
        } catch (e: Exception) {
            Either.Left(AppFailure.Empty)
        }
    }

    override suspend fun getUpcomingMovies(
        language: String,
        page: Int,
    ): Either<AppFailure, Pair<Int, ArrayList<MovieEntity>>> {
        return try {
            val result = moviesSource.getUpcomingMovies(language, page)
            Either.Right(result)
        } catch (e: AppFailure) {
            Either.Left(e)
        } catch (e: Exception) {
            Either.Left(AppFailure.Empty)
        }
    }

    override suspend fun getPopularMovies(
        language: String,
        page: Int,
    ): Either<AppFailure, Pair<Int, ArrayList<MovieEntity>>> {
        return try {
            val result = moviesSource.getPopularMovies(language, page)
            Either.Right(result)
        } catch (e: AppFailure) {
            Either.Left(e)
        } catch (e: Exception) {
            Either.Left(AppFailure.Empty)
        }
    }

    override suspend fun getTopRatedMovies(
        language: String,
        page: Int,
    ): Either<AppFailure, Pair<Int, ArrayList<MovieEntity>>> {
        return try {
            val result = moviesSource.getTopRatedMovies(language, page)
            Either.Right(result)
        } catch (e: AppFailure) {
            Either.Left(e)
        } catch (e: Exception) {
            Either.Left(AppFailure.Empty)
        }
    }

    override suspend fun searchMovies(
        language: String,
        page: Int,
        query: String?,
    ): Either<AppFailure, Pair<Int, ArrayList<MovieEntity>>> {
        return try {
            val result = moviesSource.searchMovies(language, page, query)
            Either.Right(result)
        } catch (e: AppFailure) {
            Either.Left(e)
        } catch (e: Exception) {
            Either.Left(AppFailure.Empty)
        }
    }

    private companion object {
        const val PAGE_SIZE = 20
    }
}