package com.ru.movieshows.data.model

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.ru.movieshows.data.utils.DateISO8601Converter
import com.ru.movieshows.domain.entity.CommentLevelSnippetEntity
import java.util.Date

data class CommentLevelSnippetModel(
    @SerializedName("channelId")
    val channelId: String?,
    @SerializedName("videoId")
    val videoId: String?,
    @SerializedName("textDisplay")
    val textDisplay: String?,
    @SerializedName("textOriginal")
    val textOriginal: String?,
    @SerializedName("authorDisplayName")
    val authorDisplayName: String?,
    @SerializedName("authorProfileImageUrl")
    val authorProfileImageUrl: String?,
    @SerializedName("authorChannelUrl")
    val authorChannelUrl: String?,
    @SerializedName("authorChannelId")
    val authorChannelId: Map<String, Any>,
    @SerializedName("canRate")
    val canRate: Boolean?,
    @SerializedName("viewerRating")
    val viewerRating: String?,
    @SerializedName("likeCount")
    val likeCount: Int?,
    @SerializedName("publishedAt")
    @JsonAdapter(value = DateISO8601Converter::class)
    val publishedAt: Date?,
    @SerializedName("updatedAt")
    val updatedAt: String?,
) {
    fun toEntity() = CommentLevelSnippetEntity(
        channelId,
        videoId,
        textDisplay,
        textOriginal,
        authorDisplayName,
        authorProfileImageUrl,
        authorChannelUrl,
        authorChannelId,
        canRate,
        viewerRating,
        likeCount,
        publishedAt,
        updatedAt,
    )
}