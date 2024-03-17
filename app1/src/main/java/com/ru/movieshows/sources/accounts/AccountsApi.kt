package com.ru.movieshows.sources.accounts

import com.ru.movieshows.sources.accounts.models.AccountModel
import com.ru.movieshows.sources.accounts.models.AuthenticationResponseModel
import com.ru.movieshows.sources.accounts.models.CreateSessionResponseModel
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AccountsApi {

    @GET("account")
    suspend fun getAccountBySessionId(@Query("session_id") sessionId: String): AccountModel

    @GET("authentication/token/new")
    suspend fun createRequestToken(): AuthenticationResponseModel

    @POST("authentication/token/validate_with_login")
    suspend fun signInByUsernameAndPassword(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("request_token") requestToken: String,
    ) : AuthenticationResponseModel

    @POST("authentication/session/new")
    suspend fun createSession(@Query("request_token") requestToken: String) : CreateSessionResponseModel

}