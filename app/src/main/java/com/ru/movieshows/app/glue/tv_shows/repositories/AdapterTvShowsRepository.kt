package com.ru.movieshows.app.glue.tv_shows.repositories

import com.ru.movieshows.app.glue.tv_shows.mappers.ReviewPaginationMapper
import com.ru.movieshows.app.glue.tv_shows.mappers.TvShowDetailsMapper
import com.ru.movieshows.app.glue.tv_shows.mappers.TvShowPaginationMapper
import com.ru.movieshows.app.glue.tv_shows.mappers.TvShowVideoMapper
import com.ru.movieshows.data.TvShowsDataRepository
import com.ru.movieshows.tv_shows.domain.entities.ReviewPagination
import com.ru.movieshows.tv_shows.domain.entities.TvShowDetails
import com.ru.movieshows.tv_shows.domain.entities.TvShowPagination
import com.ru.movieshows.tv_shows.domain.entities.Video
import com.ru.movieshows.tv_shows.domain.repositories.TvShowsRepository
import javax.inject.Inject

class AdapterTvShowsRepository @Inject constructor(
    private val tvShowsDataRepository: TvShowsDataRepository,
    private val tvShowPaginationMapper: TvShowPaginationMapper,
    private val tvShowDetailsMapper: TvShowDetailsMapper,
    private val reviewPaginationMapper: ReviewPaginationMapper,
    private val tvShowVideoMapper: TvShowVideoMapper,
) : TvShowsRepository {

    override suspend fun getTrendingTvShows(language: String, page: Int): TvShowPagination {
        val result = tvShowsDataRepository.getTrendingTvShows(
            language = language,
            page = page,
        )
        return tvShowPaginationMapper.toTvShowPagination(result)
    }

    override suspend fun getOnTheAirTvShows(language: String, page: Int): TvShowPagination {
        val result = tvShowsDataRepository.getOnTheAirTvShows(
            language = language,
            page = page,
        )
        return tvShowPaginationMapper.toTvShowPagination(result)
    }

    override suspend fun getTopRatedTvShows(language: String, page: Int): TvShowPagination {
        val result = tvShowsDataRepository.getTopRatedTvShows(
            language = language,
            page = page,
        )
        return tvShowPaginationMapper.toTvShowPagination(result)
    }

    override suspend fun getPopularTvShows(language: String, page: Int): TvShowPagination {
        val result = tvShowsDataRepository.getPopularTvShows(
            language = language,
            page = page,
        )
        return tvShowPaginationMapper.toTvShowPagination(result)
    }

    override suspend fun getTvShowDetails(language: String, id: String): TvShowDetails {
        val result = tvShowsDataRepository.getTvShowDetails(
            language = language,
            id = id,
        )
        return tvShowDetailsMapper.toTvShowDetails(result);
    }

    override suspend fun getVideosById(language: String, seriesId: String): List<Video> {
        val results = tvShowsDataRepository.getVideosById(
            language = language,
            seriesId = seriesId,
        )
        return results.map { tvShowVideoMapper.toVideo(it) }
    }

    override suspend fun getTvShowReviews(
        language: String,
        seriesId: String,
        page: Int,
    ): ReviewPagination {
        val result = tvShowsDataRepository.getTvReviews(
            language = language,
            seriesId = seriesId,
            page = page,
        )
        return reviewPaginationMapper.toReviewPagination(result);
    }

}