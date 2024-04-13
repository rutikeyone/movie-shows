package com.ru.movieshows.data.genres.api

import com.ru.movieshows.data.genres.models.GenresModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GenresApi {

    @GET("genre/movie/list")
    fun getGenres(
        @Query("language") language: String,
    ): Call<GenresModel>

}