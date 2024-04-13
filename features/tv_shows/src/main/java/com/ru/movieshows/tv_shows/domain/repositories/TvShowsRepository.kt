package com.ru.movieshows.tv_shows.domain.repositories

import com.ru.movieshows.tv_shows.domain.entities.TvShowPagination

interface TvShowsRepository {

    suspend fun getTrendingTvShows(language: String, page: Int) : TvShowPagination

    suspend fun getOnTheAirTvShows(language: String, page: Int): TvShowPagination

    suspend fun getTopRatedTvShows(language: String, page: Int): TvShowPagination

    suspend fun getPopularTvShows(language: String, page: Int): TvShowPagination

}