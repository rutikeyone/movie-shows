package com.ru.movieshows.app.glue.tv_shows.mappers

import com.ru.movieshows.app.formatters.ImageUrlFormatter
import com.ru.movieshows.data.tv_shows.models.CreatorModel
import com.ru.movieshows.tv_shows.domain.entities.Creator
import javax.inject.Inject

class CreatorMapper @Inject constructor(
    private val imageUrlFormatter: ImageUrlFormatter,
) {

    fun toCreator(model: CreatorModel): Creator {
        return Creator(
            id = model.id,
            name = model.name,
            creditId = model.creditId,
            photoPath = imageUrlFormatter.toImageUrl(model.photoPath),
        );
    }

}