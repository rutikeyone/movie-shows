package com.ru.movieshows.sources.people

import com.ru.movieshows.sources.people.entities.PersonEntity
import com.ru.movieshows.sources.people.models.PersonModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PeopleApi {

    @GET("person/{person_id}")
    suspend fun getPersonDetails(@Path("person_id") personId: String, @Query("language") language: String): PersonModel

}