package com.ru.movieshows.data.people.models

import com.google.gson.annotations.SerializedName
import java.util.Date

data class PersonModel(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("adult") val adult: Boolean?,
    @SerializedName("alsoKnownAs") val alsoKnownAs: List<String>?,
    @SerializedName("biography") val biography: String?,
    @SerializedName("popularity") val popularity: Double?,
    @SerializedName("imdb_id") val imdbId: String?,
    @SerializedName("known_for_department") val knownForDepartment: String?,
    @SerializedName("place_of_birth") val placeOfBirth: String?,
    @SerializedName("profile_path") val profilePath: String?,
    @SerializedName("birthday") val birthday: String?,
    @SerializedName("deathday") val deathday: String?,
)