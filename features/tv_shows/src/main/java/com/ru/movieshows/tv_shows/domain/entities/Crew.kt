package com.ru.movieshows.tv_shows.domain.entities

data class Crew(
    val id: Int?,
    val job: String?,
    val department: String?,
    val creditId: String?,
    val adult: Boolean,
    val knownForDepartment: String?,
    val name: String?,
    val originalName: String?,
    val popularity: Double?,
    val profilePath: String?,
)