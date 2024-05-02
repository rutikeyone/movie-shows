package com.ru.movieshows.app.glue.tv_shows.mappers

import com.ru.movieshows.data.movies.models.ProductionCompanyModel
import com.ru.movieshows.tv_shows.domain.entities.ProductionCompany
import javax.inject.Inject

class ProductionCompanyMapper @Inject constructor() {

    fun toProductionCompany(model: ProductionCompanyModel): ProductionCompany {
        return ProductionCompany(
            id = model.id,
            name = model.name,
        );
    }

}