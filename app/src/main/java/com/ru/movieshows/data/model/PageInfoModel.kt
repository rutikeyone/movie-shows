package com.ru.movieshows.data.model

import com.google.gson.annotations.SerializedName

data class PageInfoModel(
    @SerializedName("totalResults")
    val totalResults: Int?,
    @SerializedName("resultsPerPage")
    val resultsPerPage: Int?,
)