package com.ru.movieshows.data.tvshows.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.ru.ershov.data.core.ImagePreviewMapper
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@Parcelize
data class CrewModel(
    val id: Int,
    @SerializedName("job")
    val job: String?,
    @SerializedName("department")
    val department: String?,
    @SerializedName("credit_id")
    val creditId: String?,
    @SerializedName("adult")
    val adult: Boolean,
    @SerializedName("known_for_department")
    val knownForDepartment: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("original_name")
    val originalName: String?,
    val popularity: Double?,
    @SerializedName("profile_path")
    val profilePath: String?
): Parcelable {

    @Inject
    @IgnoredOnParcel
    lateinit var imagePreviewMapper: ImagePreviewMapper

    val profile: String? get() {
        return imagePreviewMapper.toPreviewImage(profilePath)
    }

}