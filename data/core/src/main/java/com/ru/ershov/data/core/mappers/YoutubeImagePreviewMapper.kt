package com.ru.ershov.data.core.mappers

import com.ru.ershov.data.core.ImagePreviewMapper

class YoutubeImagePreviewMapper: ImagePreviewMapper {

    override fun toPreviewImage(image: String?): String? {
        return if(!image.isNullOrEmpty()) {
            "https://i.ytimg.com/vi/$image/maxresdefault.jpg"
        } else {
            null
        }
    }

}