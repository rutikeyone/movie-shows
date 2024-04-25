package com.ru.movieshows.app.glue.movies.mappers

import com.ru.movieshows.data.movies.models.VideoModel
import com.ru.movieshows.movies.domain.entities.Video
import javax.inject.Inject

class VideoMapper @Inject constructor() {

    fun toVideo(model: VideoModel): Video {
        val key = model.key

        val image = if (!key.isNullOrEmpty()) {
            "https://i.ytimg.com/vi/$key/maxresdefault.jpg"
        } else {
            null
        }

        return Video(
            id = model.id,
            key = model.key,
            name = model.name,
            site = model.site,
            type = model.type,
            image = image,
        )
    }

}