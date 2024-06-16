package com.ru.movieshows.data.people.sources

import com.google.gson.Gson
import com.ru.movieshows.data.BaseRetrofitSource
import com.ru.movieshows.data.people.services.PeopleService
import com.ru.movieshows.data.people.models.PersonModel
import kotlinx.coroutines.delay
import javax.inject.Inject

class PeopleSourceImpl @Inject constructor(
    private val peopleService: PeopleService,
    private val gson: Gson,
) : PeopleSource, BaseRetrofitSource(gson) {

    override suspend fun getPersonDetails(
        personId: String,
        language: String,
    ): PersonModel {
        return peopleService.getPersonDetails(
            personId = personId,
            language = language,
        ).awaitResult { it }
    }

}