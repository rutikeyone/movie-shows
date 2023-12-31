package com.ru.movieshows.domain.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReviewEntity(
    val id: String?,
    val author: String?,
    val content: String?,
    val authorDetails: AuthorDetailsEntity?
): Parcelable