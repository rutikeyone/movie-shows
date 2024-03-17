package com.ru.movieshows.sources.tvshows.models

import com.google.gson.annotations.SerializedName
import com.ru.movieshows.sources.tvshows.entities.ProductionCompanyEntity

data class ProductionCompanyModel(
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val name: String?
) {
    fun toEntity(): ProductionCompanyEntity = ProductionCompanyEntity(id, name)
}