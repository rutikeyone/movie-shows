package com.ru.movieshows.domain.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AuthorDetailsEntity(
    val avatar: String?,
    val rating: Double?
): Parcelable