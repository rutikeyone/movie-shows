package com.ru.movieshows.data.tvshows.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CrewModel(
    val id: Int,
    val job: String?,
    val department: String?,
    @SerializedName("credit_id")
    val creditId: String?,
    val adult: Boolean,
    @SerializedName("known_for_department")
    val knownForDepartment: String?,
    val name: String?,
    @SerializedName("original_name")
    val originalName: String?,
    val popularity: Double?,
    @SerializedName("profile_path")
    val profilePath: String?
): Parcelable