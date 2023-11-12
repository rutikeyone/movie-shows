package com.ru.movieshows.data.model

import com.google.gson.annotations.SerializedName
import com.ru.movieshows.domain.entity.ProductionCompanyEntity

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