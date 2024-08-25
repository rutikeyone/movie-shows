package com.ru.movieshows.data.people

import com.ru.movieshows.data.IODispatcher
import com.ru.movieshows.data.PeopleDataRepository
import com.ru.movieshows.data.people.models.PersonModel
import com.ru.movieshows.data.people.sources.PeopleSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PeopleDataRepositoryImpl @Inject constructor(
    private val peopleSource: PeopleSource,
    @IODispatcher private val dispatcher: CoroutineDispatcher,
) : PeopleDataRepository {

    override suspend fun getPersonDetails(
        personId: String,
        language: String,
    ): PersonModel {
        return withContext(dispatcher) {
            peopleSource.getPersonDetails(
                personId = personId,
                language = language,
            )
        }
    }

    override suspend fun getPersonImages(
        personId: String,
    ): List<String>? {
        return withContext(dispatcher) {
            peopleSource.getPersonImages(personId)
        }
    }

}