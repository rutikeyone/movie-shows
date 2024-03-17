package com.ru.movieshows.sources.tvshows.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.ru.movieshows.sources.tvshows.entities.CrewEntity
import kotlinx.parcelize.Parcelize

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
) : Parcelable {
    fun toEntity(imageUrl: String): CrewEntity {
        val profilePath = if(this.profilePath != null) imageUrl + this.profilePath else null

        return CrewEntity(
            id,
            job,
            department,
            creditId,
            adult,
            knownForDepartment,
            name,
            originalName,
            popularity,
            profilePath,
        )
    }
}
