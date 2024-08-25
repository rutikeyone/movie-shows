package com.ru.movieshows.tv_shows.domain.repositories

import androidx.paging.PagingData
import com.ru.movieshows.tv_shows.domain.entities.Episode
import com.ru.movieshows.tv_shows.domain.entities.Review
import com.ru.movieshows.tv_shows.domain.entities.ReviewPagination
import com.ru.movieshows.tv_shows.domain.entities.Season
import com.ru.movieshows.tv_shows.domain.entities.TvShow
import com.ru.movieshows.tv_shows.domain.entities.TvShowDetails
import com.ru.movieshows.tv_shows.domain.entities.TvShowPagination
import com.ru.movieshows.tv_shows.domain.entities.TvShowSearch
import com.ru.movieshows.tv_shows.domain.entities.Video
import kotlinx.coroutines.flow.Flow

interface TvShowsRepository {

    fun searchPagedMovies(
        language: String,
        query: String? = null,
    ): Flow<PagingData<TvShow>>

    suspend fun getTrendingTvShows(
        language: String,
        page: Int,
    ): TvShowPagination

    suspend fun getOnTheAirTvShows(
        language: String,
        page: Int,
    ): TvShowPagination

    suspend fun getTopRatedTvShows(
        language: String,
        page: Int,
    ): TvShowPagination

    suspend fun getPopularTvShows(
        language: String,
        page: Int,
    ): TvShowPagination

    suspend fun getTvShowDetails(
        language: String,
        id: String,
    ): TvShowDetails

    suspend fun getImagesByTvShowId(
        id: String,
    ): List<String>?

    suspend fun getVideosByTvShowId(
        language: String,
        id: String,
    ): List<Video>

    suspend fun getSimilarTvShows(
        language: String,
        id: String,
        page: Int,
    ): TvShowPagination

    suspend fun getRecommendationsTvShows(
        language: String,
        id: String,
        page: Int,
    ): TvShowPagination

    suspend fun getTvShowReviews(
        language: String,
        seriesId: String,
        page: Int,
    ): ReviewPagination

    fun getPagedPopularTvShows(
        language: String,
    ): Flow<PagingData<TvShow>>

    fun getPagedTopRatedTvShows(
        language: String,
    ): Flow<PagingData<TvShow>>

    fun getPagedTheAirTvShows(
        language: String,
    ): Flow<PagingData<TvShow>>

    fun getPagedTvShowReviews(
        language: String,
        id: String,
    ): Flow<PagingData<Review>>

    suspend fun getSeason(
        language: String,
        seriesId: String,
        seasonNumber: String,
    ): Season

    suspend fun getVideosBySeasonNumber(
        language: String,
        seriesId: String,
        seasonNumber: String,
    ): List<Video>

    suspend fun getImagesBySeasonNumber(
        language: String,
        seriesId: String,
        seasonNumber: String,
    ): List<String>?

    suspend fun getEpisodeByNumber(
        language: String,
        seriesId: String,
        seasonNumber: String,
        episodeNumber: Int,
    ): Episode

    suspend fun getVideoByEpisodeId(
        language: String,
        seriesId: String,
        seasonNumber: String,
        episodeNumber: Int,
    ): List<Video>

    suspend fun insertTvShowSearch(
        tvShow: TvShow,
        locale: String,
    )

    suspend fun deleteTvShowSearch(
        id: Long,
    )

    fun getAllTvShowsSearch(
        locale: String,
    ): Flow<List<TvShowSearch>>

    suspend fun getImagesByEpisodeId(
        seriesId: String,
        seasonNumber: String,
        episodeNumber: Int,
    ): List<String>?

}