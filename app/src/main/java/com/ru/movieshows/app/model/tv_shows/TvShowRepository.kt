package com.ru.movieshows.app.model.tv_shows

import androidx.paging.PagingData
import arrow.core.Either
import com.ru.movieshows.app.model.AppFailure
import com.ru.movieshows.sources.movies.entities.ReviewEntity
import com.ru.movieshows.sources.movies.entities.VideoEntity
import com.ru.movieshows.sources.tv_shows.entities.EpisodeEntity
import com.ru.movieshows.sources.tv_shows.entities.SeasonEntity
import com.ru.movieshows.sources.tv_shows.entities.TvShowDetailsEntity
import com.ru.movieshows.sources.tv_shows.entities.TvShowsEntity
import kotlinx.coroutines.flow.Flow

interface TvShowRepository {
    fun searchPagedMovies(language: String = "en_US", query: String? = null): Flow<PagingData<TvShowsEntity>>
    fun getPagedTvReviews(language: String = "en_US", seriesId: String): Flow<PagingData<ReviewEntity>>
    suspend fun getVideosByMovieId(language: String = "en_US", seriesId: String): Either<AppFailure, ArrayList<VideoEntity>>
    suspend fun getSeason(language: String = "en_US", seriesId: String, seasonNumber: String): Either<AppFailure, SeasonEntity>
    suspend fun getSimilarTvShows(language: String = "en_US", page: Int = 1, seriesId: String): Either<AppFailure, Pair<Int, ArrayList<TvShowsEntity>>>
    suspend fun getDiscoverTvShows(language: String = "en_US", page: Int = 1): Either<AppFailure, Pair<Int, ArrayList<TvShowsEntity>>>
    suspend fun getTvShowDetails(language: String = "en_US", id: String): Either<AppFailure, TvShowDetailsEntity>
    suspend fun getTopRatedTvShows(language: String = "en_US", page: Int = 1): Either<AppFailure, Pair<Int, ArrayList<TvShowsEntity>>>
    suspend fun getPopularTvShows(language: String = "en_US", page: Int = 1): Either<AppFailure, Pair<Int,ArrayList<TvShowsEntity>>>
    suspend fun getOnTheAirTvShows(language: String = "en_US", page: Int = 1): Either<AppFailure, Pair<Int, ArrayList<TvShowsEntity>>>
    suspend fun getPagedTheAirTvShows(language: String = "en_US"): Flow<PagingData<TvShowsEntity>>
    suspend fun getPagedTheTopRatedTvShows(language: String = "en_US"): Flow<PagingData<TvShowsEntity>>
    suspend fun getPagedPopularTvShows(language: String = "en_US"): Flow<PagingData<TvShowsEntity>>
    suspend fun getTrendingTvShows(language: String = "en_US", page: Int = 1) : Either<AppFailure, Pair<Int, ArrayList<TvShowsEntity>>>
    suspend fun getEpisodeByNumber(language: String = "en_US", seriesId: String, seasonNumber: String, episodeNumber: Int) : Either<AppFailure, EpisodeEntity>
    suspend fun getTvReviews(language: String = "en_US", seriesId: String, page: Int): Either<AppFailure, Pair<Int, ArrayList<ReviewEntity>>>
}