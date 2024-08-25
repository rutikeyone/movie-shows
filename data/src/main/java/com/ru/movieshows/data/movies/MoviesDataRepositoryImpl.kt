package com.ru.movieshows.data.movies

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ru.movieshows.core.pagination.PageLoader
import com.ru.movieshows.core.pagination.PagePagingSource
import com.ru.movieshows.data.IODispatcher
import com.ru.movieshows.data.MoviesDataRepository
import com.ru.movieshows.data.movies.models.MovieDetailsModel
import com.ru.movieshows.data.movies.models.MovieModel
import com.ru.movieshows.data.movies.models.MoviesPaginationModel
import com.ru.movieshows.data.movies.models.ReviewModel
import com.ru.movieshows.data.movies.models.ReviewsPaginationModel
import com.ru.movieshows.data.movies.models.VideoModel
import com.ru.movieshows.data.movies.room.MovieSearchDao
import com.ru.movieshows.data.movies.room.MovieSearchRoomEntity
import com.ru.movieshows.data.movies.sources.MoviesSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MoviesDataRepositoryImpl @Inject constructor(
    private val moviesSource: MoviesSource,
    private val movieSearchDao: MovieSearchDao,
    @IODispatcher private val dispatcher: CoroutineDispatcher,
) : MoviesDataRepository {

    override fun searchPagedMovies(
        language: String,
        query: String?,
    ): Flow<PagingData<MovieModel>> {

        val loader: PageLoader<List<MovieModel>> = { pageIndex ->
            val result = withContext(dispatcher) {
                moviesSource.searchMovies(
                    language = language,
                    pageIndex = pageIndex,
                    query = query,
                )
            }

            val totalPages = result.totalPages
            val movies = result.results
            Pair(movies, totalPages)
        }

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = { PagePagingSource(loader) }
        ).flow.flowOn(dispatcher)

    }

    override fun getPagedMovieReview(
        language: String,
        movieId: String,
    ): Flow<PagingData<ReviewModel>> {

        val loader: PageLoader<List<ReviewModel>> = { pageIndex ->

            val result = withContext(dispatcher) {
                moviesSource.getMovieReviews(
                    movieId = movieId,
                    language = language,
                    pageIndex = pageIndex,
                )
            }

            val totalPages = result.totalPages
            val reviews = result.results
            Pair(reviews, totalPages)
        }

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = { PagePagingSource(loader) }
        ).flow.flowOn(dispatcher)

    }

    override fun getPagedTopRatedMovies(language: String): Flow<PagingData<MovieModel>> {

        val loader: PageLoader<List<MovieModel>> = { pageIndex ->
            val result = withContext(dispatcher) {
                moviesSource.getTopRatedMovies(
                    language = language,
                    pageIndex = pageIndex,
                )
            }

            val totalPages = result.totalPages
            val movies = result.results
            Pair(movies, totalPages)
        }

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = { PagePagingSource(loader) }
        ).flow.flowOn(dispatcher)

    }

    override fun getPagedPopularMovies(language: String): Flow<PagingData<MovieModel>> {

        val loader: PageLoader<List<MovieModel>> = { pageIndex ->
            val result = withContext(dispatcher) {
                moviesSource.getPopularMovies(
                    language = language,
                    pageIndex = pageIndex,
                )
            }

            val totalPages = result.totalPages
            val movies = result.results
            Pair(movies, totalPages)
        }

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = { PagePagingSource(loader) }
        ).flow.flowOn(dispatcher)

    }

    override fun getPagedUnComingMovies(language: String): Flow<PagingData<MovieModel>> {

        val loader: PageLoader<List<MovieModel>> = { pageIndex ->
            val result = withContext(dispatcher) {
                moviesSource.getUpcomingMovies(
                    language = language,
                    pageIndex = pageIndex,
                )
            }

            val totalPages = result.totalPages
            val movies = result.results
            Pair(movies, totalPages)
        }

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = { PagePagingSource(loader) }
        ).flow.flowOn(dispatcher)

    }

    override suspend fun getMovieReviews(
        language: String,
        movieId: String,
        page: Int,
    ): ReviewsPaginationModel {
        return withContext(dispatcher) {
            moviesSource.getMovieReviews(
                language = language,
                movieId = movieId,
                pageIndex = page,
            )
        }
    }

    override suspend fun getVideosById(
        language: String,
        movieId: String,
    ): List<VideoModel> {
        return withContext(dispatcher) {
            moviesSource.getVideosById(
                movieId = movieId,
                language = language,
            )
        }
    }

    override suspend fun getSimilarMovies(
        language: String,
        movieId: String,
        page: Int,
    ): MoviesPaginationModel {
        return withContext(dispatcher) {
            moviesSource.getSimilarMovies(
                language = language,
                movieId = movieId,
                pageIndex = page,
            )
        }
    }

    override suspend fun getDiscoverMovies(
        language: String,
        page: Int,
        withGenresId: String,
    ): MoviesPaginationModel {
        return withContext(dispatcher) {
            moviesSource.getDiscoverMovies(
                language = language,
                pageIndex = page,
                withGenresId = withGenresId,
            )
        }
    }

    override suspend fun getMovieDetails(
        movieId: Int,
        language: String,
    ): MovieDetailsModel {
        return withContext(dispatcher) {
            moviesSource.getMovieDetails(
                movieId = movieId,
                language = language,
            )
        }
    }

    override suspend fun getNowPlayingMovies(
        language: String,
        page: Int,
    ): MoviesPaginationModel {
        return withContext(dispatcher) {
            moviesSource.getNowPlayingMovies(
                language = language,
                pageIndex = page,
            )
        }
    }

    override suspend fun getUpcomingMovies(
        language: String,
        page: Int,
    ): MoviesPaginationModel {
        return withContext(dispatcher) {
            moviesSource.getUpcomingMovies(
                language = language,
                pageIndex = page,
            )
        }
    }

    override suspend fun getPopularMovies(
        language: String,
        page: Int,
    ): MoviesPaginationModel {
        return withContext(dispatcher) {
            moviesSource.getPopularMovies(
                language = language,
                pageIndex = page,
            )
        }
    }

    override suspend fun getTopRatedMovies(
        language: String,
        page: Int,
    ): MoviesPaginationModel {
        return withContext(dispatcher) {
            moviesSource.getTopRatedMovies(
                language = language,
                pageIndex = page,
            )
        }
    }

    override suspend fun searchMovies(
        language: String,
        page: Int,
        query: String?,
    ): MoviesPaginationModel {
        return withContext(dispatcher) {
            moviesSource.searchMovies(
                language = language,
                pageIndex = page,
                query = query,
            )
        }
    }

    override suspend fun insertMovieSearch(movieModel: MovieModel, locale: String) {
        val movieSearch = MovieSearchRoomEntity(movieModel, locale)
        return withContext(dispatcher) {
            movieSearchDao.insertMovieSearch(movieSearch)
        }
    }

    override suspend fun deleteMovieSearch(id: Long) {
        return withContext(dispatcher) {
            movieSearchDao.deleteMovieSearch(id)
        }
    }

    override fun getAllMoviesSearch(locale: String): Flow<List<MovieSearchRoomEntity>> {
        return movieSearchDao.getAllMoviesSearch(locale)
    }

    private companion object {
        const val PAGE_SIZE = 20
    }
}