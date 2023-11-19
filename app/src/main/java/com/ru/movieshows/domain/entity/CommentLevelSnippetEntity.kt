package com.ru.movieshows.domain.entity

import java.util.Date

data class CommentLevelSnippetEntity(
    val channelId: String?,
    val videoId: String?,
    val textDisplay: String?,
    val textOriginal: String?,
    val authorDisplayName: String?,
    val authorProfileImageUrl: String?,
    val authorChannelUrl: String?,
    val authorChannelId: Map<String, Any>,
    val canRate: Boolean?,
    val viewerRating: String?,
    val likeCount: Int?,
    val publishedAt: Date?,
    val updatedAt: String?,
)