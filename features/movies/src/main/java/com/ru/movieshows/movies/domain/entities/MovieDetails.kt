package com.ru.movieshows.movies.domain.entities

import java.util.Date

data class MovieDetails(
    val id: Int?,
    val genres: List<Genre>?,
    val releaseDate: Date?,
    val overview: String?,
    val backDropPath: String?,
    val posterPath: String?,
    val rating: Double?,
    val title: String?,
    val runtime: String?,
    val productionCompanies: List<ProductionCompany>?,
)