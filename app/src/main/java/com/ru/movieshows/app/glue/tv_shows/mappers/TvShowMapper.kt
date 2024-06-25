package com.ru.movieshows.app.glue.tv_shows.mappers

import com.ru.movieshows.app.formatters.ImageUrlFormatter
import com.ru.movieshows.data.tv_shows.models.TvShowModel
import com.ru.movieshows.tv_shows.domain.entities.TvShow
import javax.inject.Inject

class TvShowMapper @Inject constructor(
    private val imageUrlFormatter: ImageUrlFormatter,
) {

    fun toTvShow(model: TvShowModel): TvShow {
        return TvShow(
            id = model.id,
            rating = model.rating,
            name = model.name,
            backDropPath = imageUrlFormatter.toImageUrl(model.backDropPath),
            posterPath = imageUrlFormatter.toImageUrl(model.posterPath),
            overview = model.overview,
        )
    }

    fun toTvShowModel(tvShow: TvShow): TvShowModel {
        return TvShowModel(
            id = tvShow.id,
            rating = tvShow.rating,
            name = tvShow.name,
            backDropPath = tvShow.backDropPath,
            posterPath = tvShow.posterPath,
            overview = tvShow.overview,
        )
    }

}