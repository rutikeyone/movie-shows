package com.ru.movieshows.data.tv_shows.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CreatorModel(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("credit_id") val creditId: String?,
    @SerializedName("profile_path") val photoPath: String?,
): Parcelable