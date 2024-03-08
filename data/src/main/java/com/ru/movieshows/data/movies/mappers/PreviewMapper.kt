package com.ru.movieshows.data.movies.mappers

interface PreviewMapper {
    fun mapToPreviewImageUrl(key: String?): String?
}