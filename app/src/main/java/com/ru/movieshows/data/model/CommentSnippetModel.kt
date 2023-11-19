package com.ru.movieshows.data.model

import com.google.gson.annotations.SerializedName
import com.ru.movieshows.domain.entity.CommentSnippetEntity

data class CommentSnippetModel(
    @SerializedName("channelId")
    val channelId: String?,
    @SerializedName("videoId")
    val videoId: String?,
    @SerializedName("canReply")
    val canReply: Boolean?,
    @SerializedName("totalReplyCount")
    val totalReplyCount: Int?,
    @SerializedName("isPublic")
    val isPublic: Boolean?,
    @SerializedName("topLevelComment")
    val topLevelComment: CommentLevelModel?,
) {
    fun toEntity() = CommentSnippetEntity(
        channelId,
        videoId,
        canReply,
        totalReplyCount,
        isPublic,
        topLevelComment?.toEntity(),
    )
}