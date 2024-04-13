package com.ru.movieshows.data.genres.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GenreModel(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
) : Parcelable