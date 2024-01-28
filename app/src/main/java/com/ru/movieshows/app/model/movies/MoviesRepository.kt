package com.ru.movieshows.app.model.movies

import androidx.paging.PagingData
import arrow.core.Either
import com.ru.movieshows.app.model.AppFailure
import com.ru.movieshows.sources.movies.entities.MovieDetailsEntity
import com.ru.movieshows.sources.movies.entities.MovieEntity
import com.ru.movieshows.sources.movies.entities.ReviewEntity
import com.ru.movieshows.sources.movies.entities.VideoEntity
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {

    fun searchPagedMovies(language: String = "en_US", query: String? = null) : Flow<PagingData<MovieEntity>>

    fun getPagedMovieReview(language: String = "en_US", movieId: String): Flow<PagingData<ReviewEntity>>

    fun getPagedTopRatedMovies(language: String = "en_US") : Flow<PagingData<MovieEntity>>

    fun getPagedPopularMovies(language: String = "en_US") : Flow<PagingData<MovieEntity>>

    fun getPagedUnComingMovies(language: String = "en_US") : Flow<PagingData<MovieEntity>>

    suspend fun getMovieReviews(language: String = "en_US", movieId: String, page: Int): Either<AppFailure, Pair<Int, ArrayList<ReviewEntity>>>

    suspend fun getVideosById(language: String = "en_US", movieId: String): Either<AppFailure, ArrayList<VideoEntity>>

    suspend fun getSimilarMovies(language: String = "en_US", movieId: String, page: Int = 1): Either<AppFailure, ArrayList<MovieEntity>>

    suspend fun getDiscoverMovies(language: String = "en_US", page: Int = 1, withGenresId: String): Either<AppFailure, ArrayList<MovieEntity>>

    suspend fun getMovieDetails(movieId: Int, language: String = "en_US"): Either<AppFailure, MovieDetailsEntity>

    suspend fun getNowPlayingMovies(language: String = "en_US", page: Int = 1): Either<AppFailure, ArrayList<MovieEntity>>

    suspend fun getUpcomingMovies(language: String = "en_US", page: Int = 1): Either<AppFailure, Pair<Int, ArrayList<MovieEntity>>>

    suspend fun getPopularMovies(language: String = "en_US", page: Int = 1): Either<AppFailure, Pair<Int, ArrayList<MovieEntity>>>

    suspend fun getTopRatedMovies(language: String = "en_US", page: Int = 1): Either<AppFailure, Pair<Int, ArrayList<MovieEntity>>>

    suspend fun searchMovies(language: String = "en_US", page: Int = 1, query: String? = null) : Either<AppFailure, Pair<Int, ArrayList<MovieEntity>>>

}