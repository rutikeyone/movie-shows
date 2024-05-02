package com.ru.movieshows.app.glue.tv_shows.mappers

import android.annotation.SuppressLint
import com.ru.movieshows.app.formatters.ImageUrlFormatter
import com.ru.movieshows.data.tv_shows.models.EpisodeModel
import com.ru.movieshows.tv_shows.domain.entities.Episode
import java.text.SimpleDateFormat
import javax.inject.Inject

class EpisodeMapper @Inject constructor(
    private val imageUrlFormatter: ImageUrlFormatter,
    private val crewMapper: CrewMapper,
) {

    @SuppressLint("SimpleDateFormat")
    fun toEpisode(model: EpisodeModel): Episode {
        val simpleDateFormatter = SimpleDateFormat("yyyy-MM-dd")

        val modelAirDate = model.airDate
        val airDate = if (modelAirDate != null) {
            simpleDateFormatter.parse(modelAirDate)
        } else {
            null
        }

        return Episode(
            id = model.id,
            airDate = airDate,
            seasonNumber = model.seasonNumber,
            episodeNumber = model.episodeNumber,
            name = model.name,
            showId = model.showId,
            stillPath = imageUrlFormatter.toImageUrl(model.still),
            rating = model.rating,
            overview = model.overview,
            crew = model.crew?.map { crewMapper.toCrew(it) }
        );
    }

}