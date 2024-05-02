package com.ru.movieshows.app.glue.tv_shows.mappers

import android.annotation.SuppressLint
import com.ru.movieshows.app.formatters.ImageUrlFormatter
import com.ru.movieshows.data.tv_shows.models.TvShowDetailsModel
import com.ru.movieshows.tv_shows.domain.entities.TvShowDetails
import java.text.SimpleDateFormat
import javax.inject.Inject

class TvShowDetailsMapper @Inject constructor(
    private val imageUrlFormatter: ImageUrlFormatter,
    private val tvShowGenreMapper: TvShowGenreMapper,
    private val creatorMapper: CreatorMapper,
    private val seasonMapper: SeasonMapper,
    private val productionCompanyMapper: ProductionCompanyMapper,
) {

    @SuppressLint("SimpleDateFormat")
    fun toTvShowDetails(model: TvShowDetailsModel): TvShowDetails {
        val simpleDateFormatter = SimpleDateFormat("yyyy-MM-dd")

        val modelFirstAirDate = model.firstAirDate
        val firstAirDate = if(modelFirstAirDate != null) {
            simpleDateFormatter.parse(modelFirstAirDate)
        } else {
            null
        }

        return TvShowDetails(
            id = model.id,
            genres =  model.genres?.map { tvShowGenreMapper.toGenre(it) },
            firstAirDate = firstAirDate,
            overview = model.overview,
            backDropPath = imageUrlFormatter.toImageUrl(model.backDropPath),
            posterPath = imageUrlFormatter.toImageUrl(model.poster),
            rating = model.rating,
            name = model.name,
            numberOfEpisodes = model.numberOfEpisodes,
            numberOfSeasons = model.numberOfSeasons,
            createdBy = model.createdBy?.map { creatorMapper.toCreator(it) },
            seasons = model.seasons?.map { seasonMapper.toSeason(it) },
            productionCompanies = model.productionCompanies?.map { productionCompanyMapper.toProductionCompany(it) }
        );
    }

}