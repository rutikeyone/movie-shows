package com.ru.movieshows.data.movies.models

import com.google.gson.annotations.SerializedName

data class ProductionCompanyModel(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
)