package com.ru.movieshows.sources.movies.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReviewEntity(
    val id: String?,
    val author: String?,
    val content: String?,
    val authorDetails: AuthorDetailsEntity?
): Parcelable