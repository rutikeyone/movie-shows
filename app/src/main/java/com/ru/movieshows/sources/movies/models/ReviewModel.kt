package com.ru.movieshows.sources.movies.models

import com.google.gson.annotations.SerializedName
import com.ru.movieshows.sources.movies.entities.ReviewEntity

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