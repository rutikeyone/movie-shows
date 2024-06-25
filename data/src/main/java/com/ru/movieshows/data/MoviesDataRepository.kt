package com.ru.movieshows.data

import androidx.paging.PagingData
import com.ru.movieshows.data.movies.models.MovieDetailsModel
import com.ru.movieshows.data.movies.models.MovieModel
import com.ru.movieshows.data.movies.models.MoviesPaginationModel
import com.ru.movieshows.data.movies.models.ReviewModel
import com.ru.movieshows.data.movies.models.ReviewsPaginationModel
import com.ru.movieshows.data.movies.models.VideoModel
import com.ru.movieshows.data.movies.room.MovieSearchRoomEntity
import kotlinx.coroutines.flow.Flow

interface MoviesDataRepository {

    fun searchPagedMovies(
        language: String = "en_US",
        query: String? = null,
    ): Flow<PagingData<MovieModel>>

    fun getPagedMovieReview(
        language: String = "en_US",
        movieId: String,
    ): Flow<PagingData<ReviewModel>>

    fun getPagedTopRatedMovies(
        language: String = "en_US",
    ): Flow<PagingData<MovieModel>>

    fun getPagedPopularMovies(
        language: String = "en_US",
    ): Flow<PagingData<MovieModel>>

    fun getPagedUnComingMovies(
        language: String = "en_US",
    ): Flow<PagingData<MovieModel>>

    suspend fun getMovieReviews(
        language: String = "en_US",
        movieId: String,
        page: Int,
    ): ReviewsPaginationModel

    suspend fun getVideosById(
        language: String = "en_US",
        movieId: String,
    ): List<VideoModel>

    suspend fun getSimilarMovies(
        language: String = "en_US",
        movieId: String,
        page: Int = 1,
    ): MoviesPaginationModel

    suspend fun getDiscoverMovies(
        language: String = "en_US",
        page: Int = 1,
        withGenresId: String,
    ): MoviesPaginationModel

    suspend fun getMovieDetails(
        movieId: Int,
        language: String = "en_US",
    ): MovieDetailsModel

    suspend fun getNowPlayingMovies(
        language: String = "en_US",
        page: Int = 1,
    ): MoviesPaginationModel

    suspend fun getUpcomingMovies(
        language: String = "en_US",
        page: Int = 1,
    ): MoviesPaginationModel

    suspend fun getPopularMovies(
        language: String = "en_US",
        page: Int = 1,
    ): MoviesPaginationModel

    suspend fun getTopRatedMovies(
        language: String = "en_US",
        page: Int = 1,
    ): MoviesPaginationModel

    suspend fun searchMovies(
        language: String = "en_US",
        page: Int = 1,
        query: String? = null,
    ): MoviesPaginationModel

    suspend fun insertMovieSearch(movieModel: MovieModel, locale: String)

    suspend fun deleteMovieSearch(id: Long)

    fun getAllMoviesSearch(locale: String): Flow<List<MovieSearchRoomEntity>>

}