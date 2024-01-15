package com.ru.movieshows.sources.movies.models

import com.google.gson.annotations.SerializedName

data class GetReviewsResponseModels(
    @SerializedName("id")
    val id: String,
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: ArrayList<ReviewModel>,
    @SerializedName("total_pages")
    val totalPages: Int,
)