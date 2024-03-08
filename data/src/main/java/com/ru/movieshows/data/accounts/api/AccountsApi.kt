package com.ru.movieshows.data.accounts.api

import com.ru.movieshows.data.accounts.models.AccountDataModel
import com.ru.movieshows.data.accounts.models.AuthenticationModel
import com.ru.movieshows.data.accounts.models.CreateSessionModel
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AccountsApi {

    @GET("account")
    suspend fun getAccountBySessionId(
        @Query("session_id") sessionId: String
    ): AccountDataModel

    @GET("authentication/token/new")
    suspend fun createRequestToken(): AuthenticationModel

    @POST("authentication/token/validate_with_login")
    suspend fun createSessionByUsernameAndPassword(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("request_token") requestToken: String,
    ): AuthenticationModel

    @POST("authentication/session/new")
    suspend fun createSession(
        @Query("request_token") requestToken: String,
    ): CreateSessionModel

}