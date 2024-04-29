package com.ru.movieshows.data.accounts.services

import com.ru.movieshows.data.accounts.models.AccountDataModel
import com.ru.movieshows.data.accounts.models.AuthenticationModel
import com.ru.movieshows.data.accounts.models.CreateSessionModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AccountsService {

    @GET("account")
    fun getAccountBySessionId(
        @Query("session_id") sessionId: String,
    ): Call<AccountDataModel>

    @GET("authentication/token/new")
    fun createRequestToken(): Call<AuthenticationModel>

    @POST("authentication/token/validate_with_login")
    fun createSessionByUsernameAndPassword(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("request_token") requestToken: String,
    ): Call<AuthenticationModel>

    @POST("authentication/session/new")
    fun createSession(
        @Query("request_token") requestToken: String,
    ): Call<CreateSessionModel>

}