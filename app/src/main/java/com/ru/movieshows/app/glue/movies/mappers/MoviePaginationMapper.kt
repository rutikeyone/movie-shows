package com.ru.movieshows.app.glue.movies.mappers

import com.ru.movieshows.data.movies.models.MoviesPaginationModel
import com.ru.movieshows.movies.domain.entities.MoviesPagination
import javax.inject.Inject

class MoviePaginationMapper @Inject constructor(
    private val movieMapper: MovieMapper,
) {

    fun toMoviePagination(model: MoviesPaginationModel): MoviesPagination {
        return MoviesPagination(
            page = model.page,
            results = model.results.map { movieMapper.toMovie(it) },
            totalPages = model.totalPages,
        )
    }

}