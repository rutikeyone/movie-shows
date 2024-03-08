package com.ru.movieshows.data.movies.models

import com.google.gson.annotations.SerializedName

data class ReviewModel(
    val id: String?,
    val author: String?,
    val content: String?,
    @SerializedName("author_details")
    val authorDetails: AuthorModel?,
)