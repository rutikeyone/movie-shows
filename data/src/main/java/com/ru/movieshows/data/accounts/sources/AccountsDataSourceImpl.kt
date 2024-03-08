package com.ru.movieshows.data.accounts.sources

import com.ru.movieshows.core.AuthException
import com.ru.movieshows.data.accounts.api.AccountsApi
import com.ru.movieshows.data.accounts.models.AccountDataModel
import com.ru.movieshows.data.settings.SettingsDataSource
import javax.inject.Inject

class AccountsDataSourceImpl @Inject constructor(
    private val accountsApi: AccountsApi,
    private val settingsDataSource: SettingsDataSource,
) : AccountsDataSource {

    override suspend fun getAccount(): AccountDataModel {
        val sessionId = settingsDataSource.getCurrentSessionId() ?: throw AuthException()
        return accountsApi.getAccountBySessionId(sessionId)
    }

    override suspend fun createRequestToken(): String {
        val response = accountsApi.createRequestToken()
        return response.requestToken
    }

    override suspend fun createSessionByUsernameAndPassword(
        username: String,
        password: String,
        requestToken: String,
    ): String {
        val response = accountsApi.createSessionByUsernameAndPassword(
            username = username,
            password = password,
            requestToken = requestToken
        )
        return response.requestToken
    }

    override suspend fun createSession(requestToken: String): String {
        val response = accountsApi.createSession(requestToken)
        return response.sessionId
    }

}