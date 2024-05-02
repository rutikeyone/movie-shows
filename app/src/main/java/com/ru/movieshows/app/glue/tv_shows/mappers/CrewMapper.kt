package com.ru.movieshows.app.glue.tv_shows.mappers

import com.ru.movieshows.app.formatters.ImageUrlFormatter
import com.ru.movieshows.data.tv_shows.models.CrewModel
import com.ru.movieshows.tv_shows.domain.entities.Crew
import javax.inject.Inject

class CrewMapper @Inject constructor(
    private val imageUrlFormatter: ImageUrlFormatter,
) {

    fun toCrew(model: CrewModel): Crew {
        return Crew(
            id = model.id,
            job = model.job,
            department = model.department,
            creditId = model.creditId,
            adult = model.adult,
            knownForDepartment = model.knownForDepartment,
            name = model.name,
            originalName = model.originalName,
            popularity = model.popularity,
            profilePath = imageUrlFormatter.toImageUrl(model.profilePath),
        );
    }

}