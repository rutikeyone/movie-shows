package com.ru.movieshows.app.glue.tv_shows.repositories

import androidx.paging.PagingData
import androidx.paging.map
import com.ru.movieshows.app.glue.tv_shows.mappers.ReviewMapper
import com.ru.movieshows.app.glue.tv_shows.mappers.ReviewPaginationMapper
import com.ru.movieshows.app.glue.tv_shows.mappers.TvShowDetailsMapper
import com.ru.movieshows.app.glue.tv_shows.mappers.TvShowMapper
import com.ru.movieshows.app.glue.tv_shows.mappers.TvShowPaginationMapper
import com.ru.movieshows.app.glue.tv_shows.mappers.SeasonMapper
import com.ru.movieshows.app.glue.tv_shows.mappers.VideoMapper
import com.ru.movieshows.data.TvShowsDataRepository
import com.ru.movieshows.tv_shows.domain.entities.Review
import com.ru.movieshows.tv_shows.domain.entities.ReviewPagination
import com.ru.movieshows.tv_shows.domain.entities.Season
import com.ru.movieshows.tv_shows.domain.entities.TvShow
import com.ru.movieshows.tv_shows.domain.entities.TvShowDetails
import com.ru.movieshows.tv_shows.domain.entities.TvShowPagination
import com.ru.movieshows.tv_shows.domain.entities.Video
import com.ru.movieshows.tv_shows.domain.repositories.TvShowsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AdapterTvShowsRepository @Inject constructor(
    private val tvShowsDataRepository: TvShowsDataRepository,
    private val tvShowPaginationMapper: TvShowPaginationMapper,
    private val tvShowDetailsMapper: TvShowDetailsMapper,
    private val reviewPaginationMapper: ReviewPaginationMapper,
    private val videoMapper: VideoMapper,
    private val tvShowMapper: TvShowMapper,
    private val reviewMapper: ReviewMapper,
    private val seasonMapper: SeasonMapper,
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
        return results.map { videoMapper.toVideo(it) }
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

    override fun getPagedPopularTvShows(language: String): Flow<PagingData<TvShow>> {
        return tvShowsDataRepository.getPagedPopularTvShows(language).map { pagingData ->
            pagingData.map { tvShowModel -> tvShowMapper.toTvShow(tvShowModel) }
        }
    }

    override fun getPagedTopRatedTvShows(language: String): Flow<PagingData<TvShow>> {
        return tvShowsDataRepository.getPagedTopRatedTvShows(language).map { pagingData ->
            pagingData.map { tvShowModel -> tvShowMapper.toTvShow(tvShowModel) }
        }
    }

    override fun getPagedTheAirTvShows(language: String): Flow<PagingData<TvShow>> {
        return tvShowsDataRepository.getPagedTheAirTvShows(language).map { pagingData ->
            pagingData.map { tvShowModel -> tvShowMapper.toTvShow(tvShowModel) }
        }
    }

    override fun getPagedTvShowReviews(language: String, id: String): Flow<PagingData<Review>> {
        return tvShowsDataRepository.getPagedTvShowReviews(
            language = language,
            seriesId = id,
        ).map { pagingData ->
            pagingData.map { reviewModel ->
                reviewMapper.toReview(reviewModel)
            }
        }
    }

    override suspend fun getSeason(
        language: String,
        seriesId: String,
        seasonNumber: String,
    ): Season {
        val result = tvShowsDataRepository.getSeason(
            language = language,
            seriesId = seriesId,
            seasonNumber = seasonNumber
        )

        return seasonMapper.toSeason(result)
    }

}