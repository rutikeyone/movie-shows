package com.ru.movieshows.sources.genres

import com.ru.movieshows.sources.genres.models.GenresResponseModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GenresApi {

    @GET("genre/movie/list")
    suspend fun getGenres(@Query("language") language: String): Response<GenresResponseModel>

}