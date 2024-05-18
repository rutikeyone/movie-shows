package com.ru.movieshows.tv_shows.domain.repositories

import androidx.paging.PagingData
import com.ru.movieshows.tv_shows.domain.entities.Review
import com.ru.movieshows.tv_shows.domain.entities.ReviewPagination
import com.ru.movieshows.tv_shows.domain.entities.TvShow
import com.ru.movieshows.tv_shows.domain.entities.TvShowDetails
import com.ru.movieshows.tv_shows.domain.entities.TvShowPagination
import com.ru.movieshows.tv_shows.domain.entities.Video
import kotlinx.coroutines.flow.Flow

interface TvShowsRepository {

    suspend fun getTrendingTvShows(language: String, page: Int): TvShowPagination

    suspend fun getOnTheAirTvShows(language: String, page: Int): TvShowPagination

    suspend fun getTopRatedTvShows(language: String, page: Int): TvShowPagination

    suspend fun getPopularTvShows(language: String, page: Int): TvShowPagination

    suspend fun getTvShowDetails(language: String, id: String): TvShowDetails

    suspend fun getVideosById(language: String, seriesId: String): List<Video>

    suspend fun getTvShowReviews(language: String, seriesId: String, page: Int): ReviewPagination

    fun getPagedPopularTvShows(language: String): Flow<PagingData<TvShow>>

    fun getPagedTopRatedTvShows(language: String): Flow<PagingData<TvShow>>

    fun getPagedTheAirTvShows(language: String): Flow<PagingData<TvShow>>

}