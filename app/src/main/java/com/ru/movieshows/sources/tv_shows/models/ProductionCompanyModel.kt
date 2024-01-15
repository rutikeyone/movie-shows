package com.ru.movieshows.sources.tv_shows.models

import com.google.gson.annotations.SerializedName
import com.ru.movieshows.sources.tv_shows.entities.ProductionCompanyEntity

data class ProductionCompanyModel(
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val name: String?
) {
    fun toEntity(): ProductionCompanyEntity = ProductionCompanyEntity(
        id,
        name,
    )
}