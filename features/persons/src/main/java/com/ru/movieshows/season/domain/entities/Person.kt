package com.ru.movieshows.season.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
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
 ) : Parcelable