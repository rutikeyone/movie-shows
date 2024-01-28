package com.ru.movieshows.sources.tv_shows.entities

import com.ru.movieshows.sources.genres.entities.GenreEntity
import java.util.Date

 data class TvShowDetailsEntity(
     val id: Int?,
     val genres: ArrayList<GenreEntity>?,
     val firstAirDate: Date?,
     val overview: String?,
     val backDrop: String?,
     val poster: String?,
     val rating: Double?,
     val name: String?,
     val numberOfEpisodes: Int?,
     val numberOfSeasons: Int?,
     val createdBy: ArrayList<CreatorEntity>?,
     val seasons: ArrayList<SeasonEntity>?,
     val productionCompanies: ArrayList<ProductionCompanyEntity>?,
)