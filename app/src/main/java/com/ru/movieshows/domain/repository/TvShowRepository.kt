package com.ru.movieshows.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.ru.movieshows.domain.entity.TvShowsEntity
import kotlinx.coroutines.flow.Flow

interface TvShowRepository {
    suspend fun getSimilarTvShows(language: String = "en_US", page: Int = 1, seriesId: String): Result<ArrayList<TvShowsEntity>>
    suspend fun getDiscoverTvShows(language: String = "en_US", page: Int = 1): Result<ArrayList<TvShowsEntity>>
    suspend fun getTvShowDetails(language: String = "en_US", seriesId: String): Result<TvShowsEntity>
    suspend fun getTopRatedTvShows(language: String = "en_US", page: Int = 1): Result<ArrayList<TvShowsEntity>>
    suspend fun getPopularTvShows(language: String = "en_US", page: Int = 1): Result<ArrayList<TvShowsEntity>>
    suspend fun getOnTheAirTvShows(language: String = "en_US", page: Int = 1): Result<ArrayList<TvShowsEntity>>
    suspend fun getTrendingTvShows(language: String = "en_US", page: Int = 1) : Result<ArrayList<TvShowsEntity>>
    fun searchPagedMovies(language: String = "en_US", query: String? = null): LiveData<PagingData<TvShowsEntity>>
}