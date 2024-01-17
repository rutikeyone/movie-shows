package com.ru.movieshows.app.model.people

import com.ru.movieshows.sources.people.entities.PersonEntity

interface PeopleSource {

    suspend fun getPersonDetails(personId: String, language: String = "en_US"): PersonEntity

}