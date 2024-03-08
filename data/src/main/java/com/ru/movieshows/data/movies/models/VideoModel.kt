package com.ru.movieshows.data.movies.models

import com.ru.movieshows.data.movies.mappers.PreviewMapper
import javax.inject.Inject

data class VideoModel(
    val id: String?,
    val key: String?,
    val name: String?,
    val site: String?,
    val type: String?,
) {

    @Inject
    lateinit var previewMapper: PreviewMapper

    private val previewImage: String? get() {
        return previewMapper.mapToPreviewImageUrl(key)
    }
}