package com.ru.movieshows.data.movies.models

import com.google.gson.annotations.SerializedName

data class ReviewsPaginationModel(
    val page: Int,
    val results: List<ReviewModel>,
    @SerializedName("total_pages")
    val totalPages: Int,
)