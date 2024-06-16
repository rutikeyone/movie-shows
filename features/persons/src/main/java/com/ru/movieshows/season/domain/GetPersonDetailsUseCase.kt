package com.ru.movieshows.season.domain

import com.ru.movieshows.season.domain.entities.Person
import com.ru.movieshows.season.domain.repositories.PeopleRepository
import javax.inject.Inject

class GetPersonDetailsUseCase @Inject constructor(
    private val personRepository: PeopleRepository,
) {

    suspend fun execute(
        language: String = "en_US",
        personId: String,
    ): Person {
        return personRepository.getPersonDetails(
            personId = personId,
            language = language,
        )
    }

}