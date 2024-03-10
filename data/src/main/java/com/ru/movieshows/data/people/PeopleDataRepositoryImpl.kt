package com.ru.movieshows.data.people

import com.ru.movieshows.data.PeopleDataRepository
import com.ru.movieshows.data.people.models.PersonModel
import com.ru.movieshows.data.people.sources.PeopleSource
import javax.inject.Inject

class PeopleDataRepositoryImpl @Inject constructor(
    private val peopleSource: PeopleSource,
): PeopleDataRepository {

    override suspend fun getPersonDetails(
        personId: String,
        language: String,
    ): PersonModel {
        return peopleSource.getPersonDetails(
            personId, language,
        )
    }

}