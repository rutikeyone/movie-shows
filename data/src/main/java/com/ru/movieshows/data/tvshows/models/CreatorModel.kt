package com.ru.movieshows.data.tvshows.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.ru.ershov.data.core.ImagePreviewMapper
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@Parcelize
data class CreatorModel(
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("credit_id")
    val creditId: String?,
    @SerializedName("profile_path")
    val photoPath: String?,
): Parcelable {

    @IgnoredOnParcel
    @Inject
    lateinit var imagePreviewMapper: ImagePreviewMapper

    val photo: String? get() {
        return imagePreviewMapper.toPreviewImage(photoPath)
    }

}