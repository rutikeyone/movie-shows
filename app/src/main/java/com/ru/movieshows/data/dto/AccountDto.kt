package com.ru.movieshows.data.dto

import com.ru.movieshows.data.model.AccountModel
import com.ru.movieshows.data.response.AutenticatedResponse
import com.ru.movieshows.data.response.CreateSessionResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AccountDto {
    @GET("account")
    suspend fun getAccountBySessionId(@Query("session_id") sessionId: String): Response<AccountModel>

    @GET("authentication/token/new")
    suspend fun createRequestToken(): Response<AutenticatedResponse>

    @POST("authentication/token/validate_with_login")
    suspend fun createSessionWithLogin(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("request_token") requestToken: String,
    ) : Response<AutenticatedResponse>

    @POST("authentication/session/new")
    suspend fun createSession(@Query("request_token") requestToken: String) : Response<CreateSessionResponse>
}