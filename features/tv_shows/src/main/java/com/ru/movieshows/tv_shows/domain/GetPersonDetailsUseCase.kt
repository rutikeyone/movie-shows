package com.ru.movieshows.tv_shows.domain

import com.ru.movieshows.tv_shows.domain.entities.Person
import com.ru.movieshows.tv_shows.domain.repositories.PeopleRepository
import javax.inject.Inject

class GetPersonDetailsUseCase @Inject constructor(
    private val personRepository: PeopleRepository,
) {

    suspend fun execute(
        personId: String,
        language: String = "en_US",
    ): Person {
        return personRepository.getPersonDetails(
            personId, language
        )
    }

}