package com.ru.movieshows.data.tvshows.models

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.ru.ershov.data.core.ImagePreviewMapper
import com.ru.ershov.data.core.deserializers.DateJsonDeserializer
import java.util.Date
import javax.inject.Inject

data class EpisodeModel(
    val id: String?,
    @SerializedName("air_date")
    @JsonAdapter(value = DateJsonDeserializer::class)
    val airDate: Date?,
    @SerializedName("season_number")
    val seasonNumber: Int?,
    @SerializedName("episode_number")
    val episodeNumber: Int?,
    val name: String?,
    @SerializedName("show_id")
    val showId: String?,
    @SerializedName("still_path")
    val still: String?,
    @SerializedName("vote_average")
    val rating: Double?,
    @SerializedName("overview")
    val overview: String?,
    val crew: List<CrewModel>?
) {

    @Inject
    lateinit var previewMapper: ImagePreviewMapper

    val stillPath: String? get() {
        return previewMapper.toPreviewImage(still)
    }

}