package com.ru.movieshows.app.glue.tv_shows.repositories

import com.ru.movieshows.app.glue.tv_shows.mappers.PersonMapper
import com.ru.movieshows.data.PeopleDataRepository
import com.ru.movieshows.tv_shows.domain.entities.Person
import com.ru.movieshows.tv_shows.domain.repositories.PeopleRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class AdapterPeopleRepository @Inject constructor(
    private val peopleDataRepository: PeopleDataRepository,
    private val personMapper: PersonMapper,
) : PeopleRepository {

    override suspend fun getPersonDetails(personId: String, language: String): Person {
        val result = peopleDataRepository.getPersonDetails(
            personId = personId,
            language = language,
        )
        return personMapper.toPerson(result)
    }

}