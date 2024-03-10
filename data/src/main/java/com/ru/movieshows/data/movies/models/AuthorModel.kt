package com.ru.movieshows.data.movies.models

import com.google.gson.annotations.SerializedName
import com.ru.ershov.data.core.ImagePreviewMapper
import javax.inject.Inject

data class AuthorModel(
    @SerializedName("avatar_path")
    val avatarPath: String?,
    val rating: Double?,
) {

    @Inject
    lateinit var imagePreviewMapper: ImagePreviewMapper

    val avatar: String? get() {
        return imagePreviewMapper.toPreviewImage(avatarPath)
    }

}