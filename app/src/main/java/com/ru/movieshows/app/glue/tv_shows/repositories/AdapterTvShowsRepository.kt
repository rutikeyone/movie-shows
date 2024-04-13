package com.ru.movieshows.app.glue.tv_shows.repositories

import com.ru.movieshows.app.glue.tv_shows.mappers.TvShowPaginationMapper
import com.ru.movieshows.data.TvShowsDataRepository
import com.ru.movieshows.tv_shows.domain.entities.TvShowPagination
import com.ru.movieshows.tv_shows.domain.repositories.TvShowsRepository
import javax.inject.Inject

class AdapterTvShowsRepository @Inject constructor(
    private val tvShowsDataRepository: TvShowsDataRepository,
    private val tvShowPaginationMapper: TvShowPaginationMapper,
) : TvShowsRepository {

    override suspend fun getTrendingTvShows(language: String, page: Int): TvShowPagination {
        val result = tvShowsDataRepository.getTrendingTvShows(language, page)
        return tvShowPaginationMapper.toTvShowPagination(result)
    }

    override suspend fun getOnTheAirTvShows(language: String, page: Int): TvShowPagination {
        val result = tvShowsDataRepository.getOnTheAirTvShows(language, page)
        return tvShowPaginationMapper.toTvShowPagination(result)
    }

    override suspend fun getTopRatedTvShows(language: String, page: Int): TvShowPagination {
        val result = tvShowsDataRepository.getTopRatedTvShows(language, page)
        return tvShowPaginationMapper.toTvShowPagination(result)
    }

    override suspend fun getPopularTvShows(language: String, page: Int): TvShowPagination {
        val result = tvShowsDataRepository.getPopularTvShows(language, page)
        return tvShowPaginationMapper.toTvShowPagination(result)
    }

}