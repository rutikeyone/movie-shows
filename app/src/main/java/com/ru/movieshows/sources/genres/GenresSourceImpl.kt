package com.ru.movieshows.sources.genres

import com.ru.movieshows.app.model.genres.GenresSource
import com.ru.movieshows.sources.base.BaseRetrofitSource
import com.ru.movieshows.sources.base.NetworkConfig
import com.ru.movieshows.sources.genres.entities.GenreEntity
import javax.inject.Inject

class GenresSourceImpl @Inject constructor(
    private val networkConfig: NetworkConfig,
    private val genresApi: GenresApi,
) : GenresSource, BaseRetrofitSource(networkConfig) {

    override suspend fun getGenres(language: String): ArrayList<GenreEntity> = wrapRetrofitExceptions {
        val response = genresApi.getGenres(language)
        val genreModels = response.body()!!.genres
        ArrayList(genreModels.map { it.toEntity() })
    }

}