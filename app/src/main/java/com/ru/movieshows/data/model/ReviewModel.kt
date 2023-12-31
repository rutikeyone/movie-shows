package com.ru.movieshows.data.model

import com.google.gson.annotations.SerializedName
import com.ru.movieshows.domain.entity.ReviewEntity

data class ReviewModel(
    @SerializedName("id")
    val id: String?,
    @SerializedName("author")
    val author: String?,
    @SerializedName("content")
    val content: String?,
    @SerializedName("author_details")
    val authorDetails: AuthorDetailsModel?
) {
    fun toEntity(): ReviewEntity = ReviewEntity(id, author, content, authorDetails?.toEntity())
}