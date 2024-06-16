package com.ru.movieshows.season.domain.repositories

import com.ru.movieshows.season.domain.entities.Person

interface PeopleRepository {

    suspend fun getPersonDetails(personId: String, language: String): Person

}