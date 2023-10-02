package com.ru.movieshows.domain.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TvShowsEntity(
    val id: String?,
    val rating: Double?,
    val name: String?,
    val backDrop: String?,
    val poster: String?,
    val overview: String?
): Parcelable
