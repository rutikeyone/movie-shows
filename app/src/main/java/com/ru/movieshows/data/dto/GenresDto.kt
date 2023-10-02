package com.ru.movieshows.data.dto

import com.ru.movieshows.data.response.GenresResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GenresDto {
    @GET("genre/movie/list")
    suspend fun getGenres(@Query("language") language: String): Response<GenresResponse>
}