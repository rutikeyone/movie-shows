package com.ru.movieshows.sources.movies.models

import com.google.gson.annotations.SerializedName
import com.ru.movieshows.sources.movies.entities.VideoEntity

data class VideoModel(
    @SerializedName("id")
    val id: String?,
    @SerializedName("key")
    val key: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("site")
    val site: String?,
    @SerializedName("type")
    val type: String?
) {
    private val image: String? get() {
        return if(!key.isNullOrEmpty()) {
            "https://i.ytimg.com/vi/$key/maxresdefault.jpg"
        } else {
            null
        }
    }

    fun toEntity(): VideoEntity {
        return VideoEntity(
            id,
            key,
            name,
            site,
            type,
            image
        )
    }
}
