package com.ru.movieshows.sources.people.entities

import java.util.Date

data class PersonEntity(
    val id: String?,
    val name: String?,
    val adult: Boolean?,
    val alsoKnownAs: ArrayList<String>?,
    val biography: String?,
    val popularity: Double?,
    val imdbId: String?,
    val knownForDepartment: String?,
    val placeOfBirth: String?,
    val profilePath: String?,
    val birthday: Date?,
    val deathday: Date?,
)