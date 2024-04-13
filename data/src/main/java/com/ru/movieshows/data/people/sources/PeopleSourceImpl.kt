package com.ru.movieshows.data.people.sources

import com.google.gson.Gson
import com.ru.movieshows.data.BaseRetrofitSource
import com.ru.movieshows.data.people.api.PeopleApi
import com.ru.movieshows.data.people.models.PersonModel
import javax.inject.Inject

class PeopleSourceImpl @Inject constructor(
    private val peopleApi: PeopleApi,
    private val gson: Gson,
) : PeopleSource, BaseRetrofitSource(gson) {

    override suspend fun getPersonDetails(
        personId: String,
        language: String,
    ): PersonModel {
        return peopleApi.getPersonDetails(
            personId = personId,
            language = language,
        ).awaitResult { it }
    }

}