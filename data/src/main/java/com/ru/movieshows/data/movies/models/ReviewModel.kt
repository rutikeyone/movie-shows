package com.ru.movieshows.data.movies.models

import com.google.gson.annotations.SerializedName

data class ReviewModel(
    @SerializedName("id") val id: String?,
    @SerializedName("author") val author: String?,
    @SerializedName("content") val content: String?,
    @SerializedName("author_details") val authorDetails: AuthorModel?,
)