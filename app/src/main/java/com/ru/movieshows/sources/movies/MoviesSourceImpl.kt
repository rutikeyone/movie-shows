package com.ru.movieshows.sources.movies

import com.ru.movieshows.app.model.movies.MoviesSource
import com.ru.movieshows.sources.base.BaseRetrofitSource
import com.ru.movieshows.sources.base.NetworkConfig
import com.ru.movieshows.sources.movies.entities.MovieDetailsEntity
import com.ru.movieshows.sources.movies.entities.MovieEntity
import com.ru.movieshows.sources.movies.entities.ReviewEntity
import com.ru.movieshows.sources.movies.entities.VideoEntity
import javax.inject.Inject

class MoviesSourceImpl @Inject constructor(
    private val moviesApi: MoviesApi,
    private val networkConfig: NetworkConfig,
) : MoviesSource, BaseRetrofitSource(networkConfig) {

    private val imageUrl = networkConfig.imageUrl

    override suspend fun getMovieReviews(
        movieId: String,
        language: String,
        page: Int,
    ): Pair<Int, ArrayList<ReviewEntity>> = wrapRetrofitExceptions {
        val response = moviesApi.getMovieReviews(movieId, language, page)
        val body = response.body()!!
        val reviewModels = body.results
        val reviewEntities = ArrayList(reviewModels.map { it.toEntity(imageUrl) })
        val totalPages = body.totalPages
        Pair(totalPages, reviewEntities)
    }

    override suspend fun getVideosById(
        movieId: String,
        language: String,
    ): ArrayList<VideoEntity> = wrapRetrofitExceptions {
        val response = moviesApi.getVideosByMoviesId(movieId, language)
        val body = response.body()!!
        val videoModels = body.results
        ArrayList(videoModels.map { it.toEntity() })
    }

    override suspend fun getSimilarMovies(
        movieId: String,
        language: String,
        page: Int,
    ): ArrayList<MovieEntity> = wrapRetrofitExceptions {
        val response = moviesApi.getSimilarMovies(movieId, language, page)
        val movieModels = response.body()!!.results
        ArrayList(movieModels.map { it.toEntity(imageUrl) })
    }

    override suspend fun getDiscoverMovies(
        language: String,
        page: Int,
        withGenresId: String,
    ): ArrayList<MovieEntity> = wrapRetrofitExceptions {
        val response = moviesApi.getDiscoverMovies(language, page, withGenresId)
        val movieModels = response.body()!!.results
        ArrayList(movieModels.map { it.toEntity(imageUrl) })
    }

    override suspend fun getMovieDetails(
        movieId: Int,
        language: String,
    ): MovieDetailsEntity = wrapRetrofitExceptions {
        val response = moviesApi.getMovieDetails(movieId, language)
        response.body()!!.toMovieDetailsEntity(imageUrl)
    }

    override suspend fun getNowPlayingMovies(
        language: String,
        page: Int
    ): ArrayList<MovieEntity> = wrapRetrofitExceptions {
        val response = moviesApi.getMoviesNowPlaying(language, page)
        val models = response.body()!!.results
        ArrayList(models.map { it.toEntity(imageUrl) })
    }

    override suspend fun getUpcomingMovies(
        language: String,
        page: Int
    ): Pair<Int, ArrayList<MovieEntity>> = wrapRetrofitExceptions {
        val response = moviesApi.getUpcomingMovies(language, page)
        val body = response.body()!!
        val models = body.results
        val entities = ArrayList(models.map { it.toEntity(imageUrl) })
        val totalPages = body.totalPages
        Pair(totalPages, entities)
    }

    override suspend fun getPopularMovies(
        language: String,
        page: Int
    ): Pair<Int, ArrayList<MovieEntity>> = wrapRetrofitExceptions {
        val response = moviesApi.getPopularMovies(language, page)
        val body = response.body()!!
        val models = body.results
        val entities = ArrayList(models.map { it.toEntity(imageUrl) })
        val totalPages = body.totalPages
        Pair(totalPages, entities)
    }

    override suspend fun getTopRatedMovies(
        language: String,
        page: Int
    ): Pair<Int, ArrayList<MovieEntity>> = wrapRetrofitExceptions {
        val response = moviesApi.getTopRatedMovies(language, page)
        val body = response.body()!!
        val models = body.results
        val entities = ArrayList(models.map { it.toEntity(imageUrl) })
        val totalPages = body.totalPages
        Pair(totalPages, entities)
    }

    override suspend fun searchMovies(
        language: String,
        page: Int,
        query: String?,
    ): Pair<Int, ArrayList<MovieEntity>> = wrapRetrofitExceptions {
        val response = moviesApi.searchMovies(language, page, query)
        val body = response.body()!!
        val models = body.results
        val entities = ArrayList(models.map { it.toEntity(imageUrl) })
        val totalPages = body.totalPages
        Pair(totalPages, entities)
    }

}