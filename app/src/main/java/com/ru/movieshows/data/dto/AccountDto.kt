package com.ru.movieshows.data.dto

import com.ru.movieshows.data.model.AccountModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AccountDto {
    @GET()
    suspend fun getAccountBySessionId(@Query("session_id") sessionId: String): Response<AccountModel>
}