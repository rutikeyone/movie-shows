package com.ru.movieshows.data.tvshows.models

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.ru.ershov.data.core.ImagePreviewMapper
import com.ru.ershov.data.core.deserializers.DateJsonDeserializer
import java.util.Date
import javax.inject.Inject

data class SeasonModel(
    val id: Int? = null,
    @SerializedName("episode_count")
    val episodeCount: Int?,
    val name: String?,
    val overview: String?,
    @SerializedName("season_number")
    val seasonNumber: Int?,
    @SerializedName("vote_average")
    val rating: Double?,
    @SerializedName("air_date")
    @JsonAdapter(value = DateJsonDeserializer::class)
    val airDate: Date?,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("episodes")
    val episodes: List<EpisodeModel>?,
) {

    @Inject
    lateinit var imagePreviewMapper: ImagePreviewMapper

    val poster: String? get() {
        return imagePreviewMapper.toPreviewImage(posterPath)
    }

}