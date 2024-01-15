package com.ru.movieshows.sources.tv_shows

import com.ru.movieshows.app.model.tv_shows.TvShowsSource
import com.ru.movieshows.sources.base.BaseRetrofitSource
import com.ru.movieshows.sources.base.RetrofitConfig
import com.ru.movieshows.sources.movies.entities.ReviewEntity
import com.ru.movieshows.sources.movies.entities.VideoEntity
import com.ru.movieshows.sources.tv_shows.entities.EpisodeEntity
import com.ru.movieshows.sources.tv_shows.entities.SeasonEntity
import com.ru.movieshows.sources.tv_shows.entities.TvShowDetailsEntity
import com.ru.movieshows.sources.tv_shows.entities.TvShowsEntity
import javax.inject.Inject

class TvShowsSourceImpl @Inject constructor(
    private val retrofitConfig: RetrofitConfig,
    private val tvShowsApi: TvShowsApi,
) : TvShowsSource, BaseRetrofitSource(retrofitConfig) {

    override suspend fun getVideosById(
        seriesId: String,
        language: String,
    ): ArrayList<VideoEntity> = wrapRetrofitExceptions {
        val response = tvShowsApi.getVideosByTvShowId(seriesId, language)
        val body = response.body()!!
        val videoModels = body.results
        val videoEntities = videoModels.map { it.toEntity() }
        ArrayList(videoEntities)
    }

    override suspend fun getSeason(
        seriesId: String,
        seasonNumber: String,
        language: String,
    ): SeasonEntity = wrapRetrofitExceptions {
        val seasonModel = tvShowsApi.getSeason(seriesId, seasonNumber, language)
        val seasonEntity = seasonModel.toEntity()
        seasonEntity
    }

    override suspend fun getSimilarTvShows(
        seriesId: String,
        language: String,
        page: Int,
    ): Pair<Int, ArrayList<TvShowsEntity>> = wrapRetrofitExceptions {
        val response = tvShowsApi.getSimilarTvShows(seriesId, language, page)
        val body = response.body()!!
        val models = body.result
        val entities = ArrayList(models.map { it.toEntity() })
        val totalPages = body.totalPages
        Pair(totalPages, entities)
    }

    override suspend fun getDiscoverTvShows(
        language: String,
        page: Int,
    ): Pair<Int, ArrayList<TvShowsEntity>> = wrapRetrofitExceptions {
        val response = tvShowsApi.getDiscoverTvShows(language, page)
        val body = response.body()!!
        val models = body.result
        val entities = ArrayList(models.map { it.toEntity() })
        val totalPages = body.totalPages
        Pair(totalPages, entities)
    }

    override suspend fun getTvShowDetails(
        id: String,
        language: String
    ): TvShowDetailsEntity = wrapRetrofitExceptions {
        val response = tvShowsApi.getTvShowDetails(id, language)
        val model = response.body()!!
        val entity = model.toEntity()
        entity
    }

    override suspend fun getTopRatedTvShows(
        language: String,
        page: Int
    ): Pair<Int, ArrayList<TvShowsEntity>> = wrapRetrofitExceptions{
        val response = tvShowsApi.getTopRatedTvShows(language, page)
        val body = response.body()!!
        val models = body.result
        val entities = ArrayList(models.map { it.toEntity() })
        val totalPages = body.totalPages
        Pair(totalPages, entities)
    }

    override suspend fun getPopularTvShows(
        language: String,
        page: Int,
    ): Pair<Int, ArrayList<TvShowsEntity>> = wrapRetrofitExceptions {
        val response = tvShowsApi.getPopularTvShows(language, page)
        val body = response.body()!!
        val models = body.result
        val entities = ArrayList(models.map { it.toEntity() })
        val totalPages = body.totalPages
        Pair(totalPages, entities)
    }

    override suspend fun getOnTheAirTvShows(
        language: String,
        page: Int,
    ): Pair<Int, ArrayList<TvShowsEntity>> = wrapRetrofitExceptions {
        val response = tvShowsApi.getOnTheAirTvShows(language, page)
        val body = response.body()!!
        val models = body.result
        val entities = ArrayList(models.map { it.toEntity() })
        val totalPages = body.totalPages
        Pair(totalPages, entities)
    }

    override suspend fun getTrendingTvShows(
        language: String,
        page: Int,
    ): Pair<Int, ArrayList<TvShowsEntity>> = wrapRetrofitExceptions {
        val response = tvShowsApi.getTrendingTvShows(language, page)
        val body = response.body()!!
        val models = body.result
        val entities = ArrayList(models.map { it.toEntity() })
        val totalPages = body.totalPages
        Pair(totalPages, entities)
    }

    override suspend fun searchTvShows(
        language: String,
        page: Int,
        query: String?,
    ): Pair<Int, ArrayList<TvShowsEntity>> = wrapRetrofitExceptions {
        val response = tvShowsApi.searchTvShows(language, page, query)
        val body = response.body()!!
        val models = body.result
        val entities = ArrayList(models.map { it.toEntity() })
        val totalPages = body.totalPages
        Pair(totalPages, entities)
    }

    override suspend fun getEpisodeByNumber(
        language: String,
        seriesId: String,
        seasonNumber: String,
        episodeNumber: Int,
    ): EpisodeEntity = wrapRetrofitExceptions {
        val model = tvShowsApi.getEpisodeByNumber(seriesId, seasonNumber, episodeNumber.toString(), language)
        val entity = model.toEntity()
        entity
    }

    override suspend fun getTvReviews(
        seriesId: String,
        language: String,
        page: Int,
    ): Pair<Int, ArrayList<ReviewEntity>> = wrapRetrofitExceptions {
        val response = tvShowsApi.getTvReviews(seriesId, language, page)
        val body = response.body()!!
        val models = body.results
        val entities = ArrayList(models.map { it.toEntity() })
        val totalPages = body.totalPages
        Pair(totalPages, entities)
    }

}