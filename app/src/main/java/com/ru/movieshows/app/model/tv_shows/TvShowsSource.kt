package com.ru.movieshows.app.model.tv_shows

import com.ru.movieshows.sources.movies.entities.ReviewEntity
import com.ru.movieshows.sources.movies.entities.VideoEntity
import com.ru.movieshows.sources.tv_shows.entities.EpisodeEntity
import com.ru.movieshows.sources.tv_shows.entities.SeasonEntity
import com.ru.movieshows.sources.tv_shows.entities.TvShowDetailsEntity
import com.ru.movieshows.sources.tv_shows.entities.TvShowsEntity

interface TvShowsSource {

    suspend fun getVideosById(seriesId: String, language: String): ArrayList<VideoEntity>

    suspend fun getSeason(seriesId: String, seasonNumber: String, language: String): SeasonEntity

    suspend fun getSimilarTvShows(seriesId: String, language: String, page: Int) : Pair<Int, ArrayList<TvShowsEntity>>

    suspend fun getDiscoverTvShows(language: String, page: Int): Pair<Int, ArrayList<TvShowsEntity>>

    suspend fun getTvShowDetails(id: String, language: String): TvShowDetailsEntity

    suspend fun getTopRatedTvShows(language: String, page: Int): Pair<Int, ArrayList<TvShowsEntity>>

    suspend fun getPopularTvShows(language: String, page: Int): Pair<Int, ArrayList<TvShowsEntity>>

    suspend fun getOnTheAirTvShows(language: String, page: Int): Pair<Int, ArrayList<TvShowsEntity>>

    suspend fun getTrendingTvShows(language: String, page: Int): Pair<Int, ArrayList<TvShowsEntity>>

    suspend fun searchTvShows(language: String, page: Int, query: String?): Pair<Int, ArrayList<TvShowsEntity>>

    suspend fun getEpisodeByNumber(
        language: String,
        seriesId: String,
        seasonNumber: String,
        episodeNumber: Int,
    ): EpisodeEntity

    suspend fun getTvReviews(seriesId: String, language: String, page: Int): Pair<Int, ArrayList<ReviewEntity>>

}