package com.ru.movieshows.data.accounts.sources

import com.ru.movieshows.data.accounts.models.AccountDataModel

interface AccountsDataSource {

    suspend fun getAccount(): AccountDataModel

    suspend fun createRequestToken(): String

    suspend fun createSessionByUsernameAndPassword(
        username: String,
        password: String,
        requestToken: String
    ): String

    suspend fun createSession(requestToken: String): String

}