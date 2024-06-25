package com.ru.movieshows.movies.domain.repositories

import androidx.paging.PagingData
import com.ru.movieshows.movies.domain.entities.Movie
import com.ru.movieshows.movies.domain.entities.MovieDetails
import com.ru.movieshows.movies.domain.entities.MovieSearch
import com.ru.movieshows.movies.domain.entities.MoviesPagination
import com.ru.movieshows.movies.domain.entities.Review
import com.ru.movieshows.movies.domain.entities.ReviewsPagination
import com.ru.movieshows.movies.domain.entities.Video
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {

    suspend fun searchPagedMovies(
        language: String,
        query: String? = null,
    ): Flow<PagingData<Movie>>

    suspend fun getNowPlayingMovies(language: String, page: Int): List<Movie>

    suspend fun getTopRatedMovies(language: String, page: Int): List<Movie>

    suspend fun getPopularMovies(language: String, page: Int): List<Movie>

    suspend fun getUpcomingMovies(language: String, page: Int): List<Movie>

    suspend fun getDiscoverMovies(
        language: String,
        page: Int,
        withGenresId: String,
    ): List<Movie>

    suspend fun getMovieDetails(language: String, movieId: Int): MovieDetails

    suspend fun getSimilarMovies(language: String, movieId: Int, page: Int): MoviesPagination

    suspend fun getMovieReviews(
        movieId: Int,
        language: String,
        pageIndex: Int,
    ): ReviewsPagination

    suspend fun getVideosById(language: String, movieId: Int): List<Video>

    fun getPagedUnComingMovies(language: String): Flow<PagingData<Movie>>

    fun getPagedTopRatedMovies(language: String): Flow<PagingData<Movie>>

    fun getPagedPopularMovies(language: String): Flow<PagingData<Movie>>

    fun getPagedMovieReviews(
        language: String,
        id: String,
    ): Flow<PagingData<Review>>

    suspend fun insertMovieSearch(movie: Movie, locale: String)

    suspend fun deleteMovieSearch(id: Long)

    fun getAllMoviesSearch(locale: String): Flow<List<MovieSearch>>

}