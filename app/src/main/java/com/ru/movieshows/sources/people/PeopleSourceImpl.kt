package com.ru.movieshows.sources.people

import com.ru.movieshows.app.model.people.PeopleSource
import com.ru.movieshows.sources.base.BaseRetrofitSource
import com.ru.movieshows.sources.base.NetworkConfig
import com.ru.movieshows.sources.people.entities.PersonEntity
import javax.inject.Inject

class PeopleSourceImpl @Inject constructor(
    private val peopleApi: PeopleApi,
    private val networkConfig: NetworkConfig,
) : PeopleSource, BaseRetrofitSource(networkConfig) {

    override suspend fun getPersonDetails(
        personId: String,
        language: String
    ): PersonEntity = wrapRetrofitExceptions {
        val personModel = peopleApi.getPersonDetails(personId, language)
        val personEntity = personModel.toEntity()
        personEntity
    }

}