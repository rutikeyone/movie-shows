package com.ru.movieshows.app.model.people

import arrow.core.Either
import com.ru.movieshows.app.model.AppFailure
import com.ru.movieshows.sources.people.entities.PersonEntity

interface PeopleRepository {

    suspend fun getPersonDetails(personId: String, language: String = "en_US"): Either<AppFailure, PersonEntity>

}