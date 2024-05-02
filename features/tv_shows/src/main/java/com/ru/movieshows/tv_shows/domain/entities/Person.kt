package com.ru.movieshows.tv_shows.domain.entities

import java.util.Date

data class Person(
    val id: String?,
    val name: String?,
    val adult: Boolean?,
    val alsoKnownAs: List<String>?,
    val biography: String?,
    val popularity: Double?,
    val imdbId: String?,
    val knownForDepartment: String?,
    val placeOfBirth: String?,
    val profilePath: String?,
    val birthday: Date?,
    val deathday: Date?,
)