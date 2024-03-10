package com.ru.ershov.data.core

interface ImagePreviewMapper {

    fun toPreviewImage(
        image: String?,
    ): String?

}