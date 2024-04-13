package com.ru.movieshows.app.glue.movies.repositories

import com.ru.movieshows.app.glue.movies.mappers.MovieMapper
import com.ru.movieshows.data.MoviesDataRepository
import com.ru.movieshows.movies.domain.entities.Movie
import com.ru.movieshows.movies.domain.repositories.MoviesRepository
import javax.inject.Inject

class AdapterMoviesRepository @Inject constructor(
    private val moviesDataRepository: MoviesDataRepository,
    private val movieMapper: MovieMapper,
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
}