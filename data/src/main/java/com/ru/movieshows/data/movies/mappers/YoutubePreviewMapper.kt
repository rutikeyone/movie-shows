package com.ru.movieshows.data.movies.mappers

class YoutubePreviewMapper : PreviewMapper {

    override fun mapToPreviewImageUrl(key: String?): String? {
        return if(!key.isNullOrEmpty()) {
            "https://i.ytimg.com/vi/$key/maxresdefault.jpg"
        } else {
            null
        }
    }

}