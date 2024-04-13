package com.ru.movieshows.app.glue.tv_shows.mappers

import com.ru.movieshows.app.glue.movies.mappers.MovieMapper
import com.ru.movieshows.data.tv_shows.models.TvShowPaginationModel
import com.ru.movieshows.tv_shows.domain.entities.TvShowPagination
import javax.inject.Inject

class TvShowPaginationMapper @Inject constructor(
    private val tvShowMapper: TvShowMapper,
) {

    fun toTvShowPagination(model: TvShowPaginationModel): TvShowPagination {
        return TvShowPagination(
            page = model.page,
            totalPages = model.totalPages,
            results = model.result.map {
                tvShowMapper.toTvShow(it)
            }
        )
    }

}