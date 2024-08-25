package com.ru.movieshows.data

import com.ru.movieshows.data.people.models.PersonModel

interface PeopleDataRepository {

    suspend fun getPersonDetails(
        personId: String,
        language: String = "en_US",
    ): PersonModel

    suspend fun getPersonImages(
        personId: String,
    ): List<String>?

}