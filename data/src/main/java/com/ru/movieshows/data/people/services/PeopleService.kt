package com.ru.movieshows.data.people.services

import com.ru.movieshows.data.people.models.PersonModel
import com.ru.movieshows.data.tv_shows.models.ImagesModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PeopleService {

    @GET("person/{person_id}")
    fun getPersonDetails(
        @Path("person_id") personId: String,
        @Query("language") language: String,
    ): Call<PersonModel>

    @GET("person/{person_id}/images")
    fun getPersonImages(
        @Path("person_id") personId: String,
    ): Call<ImagesModel>

}