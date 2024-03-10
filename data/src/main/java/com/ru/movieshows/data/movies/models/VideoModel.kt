package com.ru.movieshows.data.movies.models

import com.ru.ershov.data.core.ImagePreviewMapper
import com.ru.ershov.data.core.di.YoutubeImagePreviewMapperQualifier
import javax.inject.Inject

data class VideoModel(
    val id: String?,
    val key: String?,
    val name: String?,
    val site: String?,
    val type: String?,
) {

    @Inject
    @YoutubeImagePreviewMapperQualifier
    lateinit var previewMapper: ImagePreviewMapper

    private val previewImage: String? get() {
        return previewMapper.toPreviewImage(key)
    }

}