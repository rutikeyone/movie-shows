package com.ru.movieshows.tv_shows.domain.repositories

import com.ru.movieshows.tv_shows.domain.entities.Person

interface PeopleRepository {

    suspend fun getPersonDetails(personId: String, language: String): Person

}