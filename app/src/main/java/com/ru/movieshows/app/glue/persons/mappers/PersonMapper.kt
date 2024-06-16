package com.ru.movieshows.app.glue.persons.mappers

import android.annotation.SuppressLint
import com.ru.movieshows.app.formatters.ImageUrlFormatter
import com.ru.movieshows.data.people.models.PersonModel
import com.ru.movieshows.season.domain.entities.Person
import java.text.SimpleDateFormat
import javax.inject.Inject


class PersonMapper @Inject constructor(
    private val imageUrlFormatter: ImageUrlFormatter,
) {

    @SuppressLint("SimpleDateFormat")
    fun toPerson(model: PersonModel): Person {
        val simpleDateFormatter = SimpleDateFormat("yyyy-MM-dd")

        val modelBirthday = model.birthday
        val birthday = if (modelBirthday != null) {
            simpleDateFormatter.parse(modelBirthday)
        } else {
            null
        }

        val modelDeathday = model.birthday
        val deathday = if (modelDeathday != null) {
            simpleDateFormatter.parse(modelDeathday)
        } else {
            null
        }

        return Person(
            id = model.id,
            name = model.name,
            adult = model.adult,
            alsoKnownAs = model.alsoKnownAs,
            biography = model.biography,
            popularity = model.popularity,
            imdbId = model.imdbId,
            knownForDepartment = model.knownForDepartment,
            placeOfBirth = model.placeOfBirth,
            profilePath = imageUrlFormatter.toImageUrl(model.profilePath),
            birthday = birthday,
            deathday = deathday,
        );
    }

}