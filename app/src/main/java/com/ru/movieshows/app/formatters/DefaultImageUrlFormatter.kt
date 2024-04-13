package com.ru.movieshows.app.formatters

import javax.inject.Inject

class DefaultImageUrlFormatter @Inject constructor() : ImageUrlFormatter {

    override fun toImageUrl(image: String?): String? {
        return image?.let {
            BASE_IMAGE_URL + it
        }
    }

    private companion object {
        const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/original"
    }

}