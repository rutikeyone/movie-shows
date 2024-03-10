package com.ru.movieshows.data.people.sources

import com.ru.movieshows.data.people.api.PeopleApi
import com.ru.movieshows.data.people.models.PersonModel
import javax.inject.Inject

class PeopleSourceImpl @Inject constructor(
    private val peopleApi: PeopleApi,
): PeopleSource {

    override suspend fun getPersonDetails(
        personId: String,
        language: String,
    ): PersonModel {
        return peopleApi.getPersonDetails(
            personId,
            language,
        )
    }

}