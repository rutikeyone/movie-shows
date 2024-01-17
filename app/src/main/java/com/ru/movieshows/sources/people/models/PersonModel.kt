package com.ru.movieshows.sources.people.models

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.ru.movieshows.sources.tv_shows.converters.DateConverter
import com.ru.movieshows.sources.people.entities.PersonEntity
import java.util.Date

data class PersonModel(
    val id: String?,
    val name: String?,
    val adult: Boolean?,
    val alsoKnownAs: ArrayList<String>?,
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
    @JsonAdapter(value = DateConverter::class)
    val birthday: Date?,
    @JsonAdapter(value = DateConverter::class)
    val deathday: Date?,
) {

    fun toEntity(imageUrl: String) = PersonEntity(
        id,
        name,
        adult,
        alsoKnownAs,
        biography,
        popularity,
        imdbId,
        knownForDepartment,
        placeOfBirth,
        if(!profilePath.isNullOrEmpty()) imageUrl + profilePath else null,
        birthday,
        deathday,
    )

}