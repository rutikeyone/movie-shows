package com.ru.movieshows.domain.repository

import androidx.paging.PagingData
import com.ru.movieshows.domain.entity.MovieDetailsEntity
import com.ru.movieshows.domain.entity.MovieEntity
import com.ru.movieshows.domain.entity.ReviewEntity
import com.ru.movieshows.domain.entity.TvShowsEntity
import com.ru.movieshows.domain.entity.VideoEntity
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
    fun getPagedMovieReview(language: String = "en_US", movieId: String): Flow<PagingData<ReviewEntity>>
    suspend fun getMovieReviews(language: String = "en_US", movieId: String, page: Int): Result<ArrayList<ReviewEntity>>
    suspend fun getVideosByMovieId(language: String = "en_Us", movieId: String): Result<ArrayList<VideoEntity>>
    suspend fun getSimilarMovies(language: String = "en_US", movieId: String, page: Int = 1): Result<ArrayList<MovieEntity>>
    suspend fun getDiscoverMovies(language: String = "en_US", page: Int = 1, withGenresId: String): Result<ArrayList<MovieEntity>>
    suspend fun getMovieDetails(movieId: Int, language: String = "en_US"): Result<MovieDetailsEntity>
    suspend fun getMoviesNowPlaying(language: String = "en_US", page: Int = 1): Result<ArrayList<MovieEntity>>
    suspend fun getUpcomingMovies(language: String = "en_US", page: Int = 1): Result<ArrayList<MovieEntity>>
    suspend fun getPopularMovies(language: String = "en_US", page: Int = 1): Result<ArrayList<MovieEntity>>
    suspend fun getTopRatedMovies(language: String = "en_US", page: Int = 1): Result<ArrayList<MovieEntity>>

}