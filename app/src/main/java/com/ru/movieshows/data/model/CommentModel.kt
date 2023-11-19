package com.ru.movieshows.data.model

import com.google.gson.annotations.SerializedName
import com.ru.movieshows.domain.entity.CommentEntity

data class CommentModel(
    @SerializedName("kind")
    val kind: String?,
    @SerializedName("etag")
    val etag: String?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("snippet")
    val snippet: CommentSnippetModel?,
) {
    fun toEntity() = CommentEntity(
        kind,
        etag,
        id,
        snippet?.toEntity(),
    )
}