package com.ru.movieshows.data.people.sources

import com.ru.movieshows.data.people.models.PersonModel

interface PeopleSource {

    suspend fun getPersonDetails(
        personId: String,
        language: String,
    ): PersonModel

    suspend fun getPersonImages(
        personId: String,
    ): List<String>?

}