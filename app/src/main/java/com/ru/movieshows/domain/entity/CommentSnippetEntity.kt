package com.ru.movieshows.domain.entity

data class CommentSnippetEntity(
    val channelId: String?,
    val videoId: String?,
    val canReply: Boolean?,
    val totalReplyCount: Int?,
    val isPublic: Boolean?,
    val topLevelComment: CommentLevelEntity?,
)