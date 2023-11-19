package com.ru.movieshows.data.model

import com.google.gson.annotations.SerializedName
import com.ru.movieshows.domain.entity.CommentLevelEntity

data class CommentLevelModel(
    @SerializedName("kind")
    val kind: String?,
    @SerializedName("etag")
    val etag: String?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("snippet")
    val snippet: CommentLevelSnippetModel?,
) {
    fun toEntity() = CommentLevelEntity(
        kind,
        etag,
        id,
        snippet?.toEntity(),
    )
}