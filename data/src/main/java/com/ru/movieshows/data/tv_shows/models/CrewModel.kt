package com.ru.movieshows.data.tv_shows.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CrewModel(
    @SerializedName("id") val id: Int,
    @SerializedName("job") val job: String?,
    @SerializedName("department") val department: String?,
    @SerializedName("credit_id") val creditId: String?,
    @SerializedName("adult") val adult: Boolean,
    @SerializedName("known_for_department") val knownForDepartment: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("original_name") val originalName: String?,
    @SerializedName("popularity") val popularity: Double?,
    @SerializedName("profile_path") val profilePath: String?,
) : Parcelable