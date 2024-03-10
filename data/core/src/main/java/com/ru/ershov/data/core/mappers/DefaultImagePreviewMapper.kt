package com.ru.ershov.data.core.mappers

import com.ru.ershov.data.core.ImagePreviewMapper
import com.ru.ershov.data.core.di.BaseImageUrlQualifier
import javax.inject.Inject

class DefaultImagePreviewMapper @Inject constructor(
    @BaseImageUrlQualifier private val baseImageUrl: String,
) : ImagePreviewMapper {

    override fun toPreviewImage(image: String?): String? {
        return if(!image.isNullOrEmpty()) {
            baseImageUrl + image
        } else {
            null
        }
    }

}