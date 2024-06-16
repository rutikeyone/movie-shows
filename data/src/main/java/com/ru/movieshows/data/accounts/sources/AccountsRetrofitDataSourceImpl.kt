package com.ru.movieshows.data.accounts.sources

import com.google.gson.Gson
import com.ru.movieshows.core.NotAuthException
import com.ru.movieshows.data.BaseRetrofitSource
import com.ru.movieshows.data.accounts.services.AccountsService
import com.ru.movieshows.data.accounts.models.AccountDataModel
import com.ru.movieshows.data.settings.sources.SettingsDataSource
import kotlinx.coroutines.delay
import javax.inject.Inject

class AccountsRetrofitDataSourceImpl @Inject constructor(
    private val accountsApi: AccountsService,
    private val settingsDataSource: SettingsDataSource,
    private val gson: Gson,
) : AccountsDataSource, BaseRetrofitSource(gson) {

    override suspend fun getAccount(): AccountDataModel {
        val sessionId = settingsDataSource.getCurrentSessionId() ?: throw NotAuthException()

        delay(1000)

        return accountsApi
            .getAccountBySessionId(sessionId)
            .awaitResult { it }
    }

    override suspend fun createRequestToken(): String {
        return accountsApi
            .createRequestToken()
            .awaitResult { it.requestToken }
    }

    override suspend fun createSessionByUsernameAndPassword(
        username: String,
        password: String,
        requestToken: String,
    ): String {
        return accountsApi.createSessionByUsernameAndPassword(
            username = username,
            password = password,
            requestToken = requestToken
        ).awaitResult { it.requestToken }
    }

    override suspend fun createSession(requestToken: String): String {
        return accountsApi
            .createSession(requestToken)
            .awaitResult { it.sessionId }
    }

}
