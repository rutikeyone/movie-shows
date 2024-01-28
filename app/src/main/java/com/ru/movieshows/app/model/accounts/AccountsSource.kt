package com.ru.movieshows.app.model.accounts

import com.ru.movieshows.sources.accounts.entities.AccountEntity

interface AccountsSource {

    suspend fun getAccountBySessionId(sessionId: String): AccountEntity

    suspend fun createRequestToken(): String

    suspend fun createSessionByUsernameAndPassword(
        username: String,
        password: String,
        requestToken: String,
    ) : String

    suspend fun createSession(requestToken: String): String

}