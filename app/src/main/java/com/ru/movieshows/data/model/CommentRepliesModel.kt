package com.ru.movieshows.data.model

import com.google.gson.annotations.SerializedName
import com.ru.movieshows.domain.entity.CommentRepliesEntity

data class CommentRepliesModel (
    @SerializedName("comments")
    val comments: ArrayList<CommentLevelModel>?
) {
    fun toEntity() = CommentRepliesEntity(
        comments?.let { comments ->
            ArrayList(comments.map { it.toEntity() })
        }
    )
}