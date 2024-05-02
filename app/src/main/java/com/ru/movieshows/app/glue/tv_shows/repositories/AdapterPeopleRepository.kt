package com.ru.movieshows.app.glue.tv_shows.repositories

import com.ru.movieshows.tv_shows.domain.entities.Person
import com.ru.movieshows.tv_shows.domain.repositories.PeopleRepository
import javax.inject.Inject

class AdapterPeopleRepository @Inject constructor() : PeopleRepository {

    override suspend fun getPersonDetails(personId: String, language: String): Person {
        TODO("Not yet implemented")
    }

}