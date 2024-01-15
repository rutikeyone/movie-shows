package com.ru.movieshows.sources.tv_shows.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CrewEntity(
    val id: Int,
    val job: String?,
    val department: String?,
    val creditId: String?,
    val adult: Boolean,
    val knownForDepartment: String?,
    val name: String?,
    val originalName: String?,
    val popularity: Double?,
    val profilePath: String?
): Parcelable