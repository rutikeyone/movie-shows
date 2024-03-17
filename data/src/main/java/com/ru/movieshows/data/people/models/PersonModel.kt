package com.ru.movieshows.data.people.models

import com.google.gson.annotations.SerializedName
import java.util.Date

data class PersonModel(
    val id: String?,
    val name: String?,
    val adult: Boolean?,
    val alsoKnownAs: List<String>?,
    val biography: String?,
    val popularity: Double?,
    @SerializedName("imdb_id")
    val imdbId: String?,
    @SerializedName("known_for_department")
    val knownForDepartment: String?,
    @SerializedName("place_of_birth")
    val placeOfBirth: String?,
    @SerializedName("profile_path")
    val profilePath: String?,
    val birthday: String?,
    val deathday: String?,
)