package com.ru.movieshows.domain.entity

data class CommentEntity(
    val kind: String?,
    val etag: String?,
    val id: String?,
    val snippet: CommentSnippetEntity?,
    val replies: CommentRepliesEntity?,
) {
    val countReplies: Int? get() {
        val commentsSize = replies?.comments?.size ?: return null
        return if(commentsSize > 0) commentsSize else null
    }
}