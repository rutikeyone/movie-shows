package com.ru.movieshows.data

import androidx.paging.PagingData
import com.ru.movieshows.data.movies.models.ReviewModel
import com.ru.movieshows.data.movies.models.ReviewsPaginationModel
import com.ru.movieshows.data.movies.models.VideoModel
import com.ru.movieshows.data.tv_shows.models.EpisodeModel
import com.ru.movieshows.data.tv_shows.models.SeasonModel
import com.ru.movieshows.data.tv_shows.models.TvShowDetailsModel
import com.ru.movieshows.data.tv_shows.models.TvShowModel
import com.ru.movieshows.data.tv_shows.models.TvShowPaginationModel
import kotlinx.coroutines.flow.Flow

interface TvShowsDataRepository {

     fun searchPagedMovies(
         language: String = "en_US",
         query: String? = null,
     ): Flow<PagingData<TvShowModel>>

     fun getPagedTvShowReviews(
         language: String = "en_US",
         seriesId: String,
     ): Flow<PagingData<ReviewModel>>

     suspend fun getVideosById(
         language: String = "en_US",
         seriesId: String,
     ): List<VideoModel>

     suspend fun getSeason(
         language: String = "en_US",
         seriesId: String,
         seasonNumber: String,
     ): SeasonModel

     suspend fun getSimilarTvShows(
         language: String = "en_US",
         page: Int = 1,
         seriesId: String,
     ): TvShowPaginationModel

     suspend fun getDiscoverTvShows(
         language: String = "en_US",
         page: Int = 1,
     ): TvShowPaginationModel

     suspend fun getTvShowDetails(
         language: String = "en_US",
         id: String,
     ): TvShowDetailsModel

     suspend fun getTopRatedTvShows(
         language: String = "en_US",
         page: Int = 1,
     ): TvShowPaginationModel

     suspend fun getPopularTvShows(
         language: String = "en_US",
         page: Int = 1,
     ): TvShowPaginationModel

     suspend fun getOnTheAirTvShows(
         language: String = "en_US",
         page: Int = 1,
     ): TvShowPaginationModel

     fun getPagedTheAirTvShows(
         language: String = "en_US",
     ): Flow<PagingData<TvShowModel>>

     fun getPagedTopRatedTvShows(
         language: String = "en_US",
     ): Flow<PagingData<TvShowModel>>

     fun getPagedPopularTvShows(
         language: String = "en_US",
     ): Flow<PagingData<TvShowModel>>

     suspend fun getTrendingTvShows(
         language: String = "en_US",
         page: Int = 1,
     ) : TvShowPaginationModel

     suspend fun getEpisodeByNumber(
         language: String = "en_US",
         seriesId: String,
         seasonNumber: String,
         episodeNumber: Int,
     ) : EpisodeModel

     suspend fun getTvReviews(
         language: String = "en_US",
         seriesId: String,
         page: Int,
     ): ReviewsPaginationModel

 }