package com.ru.movieshows.app.glue.movies.repositories

import com.ru.movieshows.app.glue.movies.mappers.MovieDetailsMapper
import com.ru.movieshows.app.glue.movies.mappers.MovieMapper
import com.ru.movieshows.app.glue.movies.mappers.MoviePaginationMapper
import com.ru.movieshows.app.glue.movies.mappers.ReviewsPaginationMapper
import com.ru.movieshows.app.glue.movies.mappers.VideoMapper
import com.ru.movieshows.data.MoviesDataRepository
import com.ru.movieshows.movies.domain.entities.Movie
import com.ru.movieshows.movies.domain.entities.MovieDetails
import com.ru.movieshows.movies.domain.entities.MoviesPagination
import com.ru.movieshows.movies.domain.entities.ReviewsPagination
import com.ru.movieshows.movies.domain.entities.Video
import com.ru.movieshows.movies.domain.repositories.MoviesRepository
import javax.inject.Inject

class AdapterMoviesRepository @Inject constructor(
    private val moviesDataRepository: MoviesDataRepository,
    private val movieMapper: MovieMapper,
    private val movieDetailsMapper: MovieDetailsMapper,
    private val moviePaginationMapper: MoviePaginationMapper,
    private val reviewsPaginationMapper: ReviewsPaginationMapper,
    private val videoMapper: VideoMapper,
) : MoviesRepository {
    override suspend fun getNowPlayingMovies(language: String, page: Int): List<Movie> {
        val moviesPaginationModel = moviesDataRepository.getNowPlayingMovies(language, page)
        return moviesPaginationModel.results.map {
            movieMapper.toMovie(it)
        }
    }

    override suspend fun getTopRatedMovies(language: String, page: Int): List<Movie> {
        val moviesPaginationModel = moviesDataRepository.getTopRatedMovies(language, page)
        return moviesPaginationModel.results.map {
            movieMapper.toMovie(it)
        }
    }

    override suspend fun getPopularMovies(language: String, page: Int): List<Movie> {
        val moviesPaginationModel = moviesDataRepository.getPopularMovies(language, page)
        return moviesPaginationModel.results.map {
            movieMapper.toMovie(it)
        }
    }

    override suspend fun getUpcomingMovies(language: String, page: Int): List<Movie> {
        val moviesPaginationModel = moviesDataRepository.getUpcomingMovies(language, page)
        return moviesPaginationModel.results.map {
            movieMapper.toMovie(it)
        }
    }

    override suspend fun getDiscoverMovies(
        language: String,
        page: Int,
        withGenresId: String,
    ): List<Movie> {
        val moviesPaginationModel =
            moviesDataRepository.getDiscoverMovies(language, page, withGenresId)
        return moviesPaginationModel.results.map {
            movieMapper.toMovie(it)
        }
    }

    override suspend fun getMovieDetails(language: String, movieId: Int): MovieDetails {
        val result = moviesDataRepository.getMovieDetails(movieId, language)
        return movieDetailsMapper.toMovieDetails(result)
    }

    override suspend fun getSimilarMovies(
        language: String,
        movieId: Int,
        page: Int,
    ): MoviesPagination {
        val result = moviesDataRepository.getSimilarMovies(
            language = language,
            movieId = movieId.toString(),
            page = page,
        )
        return moviePaginationMapper.toMoviePagination(result)
    }

    override suspend fun getMovieReviews(
        movieId: Int,
        language: String,
        pageIndex: Int,
    ): ReviewsPagination {
        val result = moviesDataRepository.getMovieReviews(
            language = language,
            movieId = movieId.toString(),
            page = pageIndex,
        )
        return reviewsPaginationMapper.toReviewsPagination(result)
    }

    override suspend fun getVideosById(language: String, movieId: Int): List<Video> {
        val result = moviesDataRepository.getVideosById(
            language,
            movieId.toString()
        )
        return result.map { videoMapper.toVideo(it) }
    }

}