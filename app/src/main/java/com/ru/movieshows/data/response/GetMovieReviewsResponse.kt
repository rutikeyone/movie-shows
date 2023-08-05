package com.ru.movieshows.data.response

import com.google.gson.annotations.SerializedName
import com.ru.movieshows.data.model.ReviewModel

data class GetMovieReviewsResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: ArrayList<ReviewModel>,
    @SerializedName("total_pages")
    val totalPages: Int,
)