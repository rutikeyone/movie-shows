package com.ru.movieshows.app.glue.persons.repositories

import com.ru.movieshows.app.glue.persons.mappers.PersonMapper
import com.ru.movieshows.data.PeopleDataRepository
import com.ru.movieshows.season.domain.entities.Person
import com.ru.movieshows.season.domain.repositories.PeopleRepository
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