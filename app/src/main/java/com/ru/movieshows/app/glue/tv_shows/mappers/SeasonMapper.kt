package com.ru.movieshows.app.glue.tv_shows.mappers

import android.annotation.SuppressLint
import com.ru.movieshows.app.formatters.ImageUrlFormatter
import com.ru.movieshows.data.tv_shows.models.SeasonModel
import com.ru.movieshows.tv_shows.domain.entities.Season
import java.text.SimpleDateFormat
import javax.inject.Inject

class SeasonMapper @Inject constructor(
    private val imageUrlFormatter: ImageUrlFormatter,
    private val episodeMapper: EpisodeMapper,
) {

    @SuppressLint("SimpleDateFormat")
    fun toSeason(model: SeasonModel): Season {
        val simpleDateFormatter = SimpleDateFormat("yyyy-MM-dd")

        val modelAirDate = model.airDate
        val airDate = if (modelAirDate != null) {
            simpleDateFormatter.parse(modelAirDate)
        } else {
            null
        }

        val episodes = model.episodes?.map { episodeMapper.toEpisode(it) }

        return Season(
            id = model.id,
            name = model.name,
            overview = model.overview,
            seasonNumber = model.seasonNumber,
            rating = model.rating,
            airDate = airDate,
            posterPath = imageUrlFormatter.toImageUrl(model.posterPath),
            episodeCount = model.episodeCount,
            episodeCountValue = episodes?.size ?: model.episodeCount,
            episodes = episodes,
        )
    }

}