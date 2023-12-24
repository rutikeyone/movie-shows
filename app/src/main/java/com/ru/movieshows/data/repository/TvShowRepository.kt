package com.ru.movieshows.data.repository

import androidx.paging.PagingData
import com.ru.movieshows.domain.entity.SeasonEntity
import com.ru.movieshows.domain.entity.TvShowDetailsEntity
import com.ru.movieshows.domain.entity.TvShowsEntity
import com.ru.movieshows.domain.entity.VideoEntity
import com.ru.movieshows.domain.repository.Repository
import kotlinx.coroutines.flow.Flow

interface TvShowRepository : Repository {
    suspend fun getVideosByMovieId(language: String = "en_US", movieId: String): Result<ArrayList<VideoEntity>>
    suspend fun getSeason(language: String = "en_US", seriesId: String, seasonNumber: String): Result<SeasonEntity>
    suspend fun getSimilarTvShows(language: String = "en_US", page: Int = 1, seriesId: String): Result<ArrayList<TvShowsEntity>>
    suspend fun getDiscoverTvShows(language: String = "en_US", page: Int = 1): Result<ArrayList<TvShowsEntity>>
    suspend fun getTvShowDetails(language: String = "en_US", id: String): Result<TvShowDetailsEntity>
    suspend fun getTopRatedTvShows(language: String = "en_US", page: Int = 1): Result<ArrayList<TvShowsEntity>>
    suspend fun getPopularTvShows(language: String = "en_US", page: Int = 1): Result<ArrayList<TvShowsEntity>>
    suspend fun getOnTheAirTvShows(language: String = "en_US", page: Int = 1): Result<ArrayList<TvShowsEntity>>
    suspend fun getPagedTheAirTvShows(language: String = "en_US"): Flow<PagingData<TvShowsEntity>>
    suspend fun getPagedTheTopRatedTvShows(language: String = "en_US"): Flow<PagingData<TvShowsEntity>>
    suspend fun getPagedPopularTvShows(language: String = "en_US"): Flow<PagingData<TvShowsEntity>>
    suspend fun getTrendingTvShows(language: String = "en_US", page: Int = 1) : Result<ArrayList<TvShowsEntity>>
    fun searchPagedMovies(language: String = "en_US", query: String? = null): Flow<PagingData<TvShowsEntity>>
}