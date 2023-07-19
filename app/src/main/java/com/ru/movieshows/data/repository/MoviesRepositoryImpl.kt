package com.ru.movieshows.data.repository

import com.ru.movieshows.data.dto.MoviesDto
import com.ru.movieshows.domain.entity.MovieDetailsEntity
import com.ru.movieshows.domain.entity.MovieEntity
import com.ru.movieshows.domain.entity.ReviewEntity
import com.ru.movieshows.domain.entity.TvShowsEntity
import com.ru.movieshows.domain.entity.VideoEntity
import com.ru.movieshows.domain.repository.MoviesRepository
import com.ru.movieshows.domain.repository.exceptions.AppFailure
import kotlinx.coroutines.delay
import java.net.ConnectException
import javax.inject.Inject
import kotlin.Exception

class MoviesRepositoryImpl @Inject constructor(private val moviesDto: MoviesDto): MoviesRepository {
    override suspend fun getMovieReviews(language: String, movieId: String, ): Result<ArrayList<ReviewEntity>> {
        return try {
            val getMovieReviewsResponse = moviesDto.getMovieReviews(movieId, language)
            return if(getMovieReviewsResponse.isSuccessful && getMovieReviewsResponse.body() != null) {
                val movieModels = getMovieReviewsResponse.body()!!.results
                val movieEntities = movieModels.map { it.toEntity() }
                Result.success(ArrayList(movieEntities))
            } else {
                val movieException = AppFailure.Pure
                Result.failure(movieException)
            }
        } catch (e: Exception) {
            val movieException = AppFailure.Pure
            Result.failure(movieException)
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
        } catch (e: Exception) {
            val movieException = AppFailure.Pure
            Result.failure(movieException)
        }
    }

    override suspend fun getSimilarMovies(language: String, movieId: String, page: Int): Result<ArrayList<TvShowsEntity>> {
        return try {
            val getSimilarTvShowsResponse = moviesDto.getSimilarMovies(movieId, language, page)
            return if(getSimilarTvShowsResponse.isSuccessful && getSimilarTvShowsResponse.body() != null) {
                val similarTvShowModels = getSimilarTvShowsResponse.body()!!.results
                val similarTvShowEntities = similarTvShowModels.map { it.toEntity() }
                Result.success(ArrayList(similarTvShowEntities))
            } else {
                val movieException = AppFailure.Pure
                Result.failure(movieException)
            }
        } catch (e: Exception) {
            val movieException = AppFailure.Pure
            Result.failure(movieException)
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
        } catch (e: Exception) {
            val movieException = AppFailure.Pure
            Result.failure(movieException)
        }
    }

    override suspend fun getMovieDetails(
        movieId: String,
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
        } catch (e: Exception) {
            val movieException = AppFailure.Pure
            Result.failure(movieException)
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
        catch (e: ConnectException) {
            val movieException = AppFailure.Connection
            delay(1000)
            Result.failure(movieException)
        }
        catch (e: Exception) {
            val movieException = AppFailure.Pure
            Result.failure(movieException)
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
        } catch (e: Exception) {
            val movieException = AppFailure.Pure
            movieException.initCause(e)
            Result.failure(movieException)
        }
    }

    override suspend fun getPopularMovies(
        language: String,
        page: Int,
    ): Result<ArrayList<MovieEntity>> {
        return try {
            val getPopularMoviesResponse = moviesDto.getPopularMovies(language, page)
            return if(getPopularMoviesResponse.isSuccessful && getPopularMoviesResponse.body() != null) {
                val moviesModels = getPopularMoviesResponse.body()!!.result
                val moviesEntities = moviesModels.map { it.toEntity() }
                Result.success(ArrayList(moviesEntities))
            } else {
                val movieException = AppFailure.Pure
                Result.failure(movieException)
            }
        } catch (e: Exception) {
            val movieException = AppFailure.Pure
            Result.failure(movieException)
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
        } catch (e: Exception) {
            val movieException = AppFailure.Pure
            Result.failure(movieException)
        }
    }
}