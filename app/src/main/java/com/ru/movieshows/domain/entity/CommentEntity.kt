package com.ru.movieshows.domain.entity

data class CommentEntity(
    val kind: String?,
    val etag: String?,
    val id: String?,
    val snippet: CommentSnippetEntity?,
)