package com.ru.movieshows.sources.accounts

import com.ru.movieshows.app.model.accounts.AccountsSource
import com.ru.movieshows.sources.accounts.entities.AccountEntity
import com.ru.movieshows.sources.base.BaseRetrofitSource
import com.ru.movieshows.sources.base.RetrofitConfig
import javax.inject.Inject

class AccountsSourceImpl @Inject constructor(
    private val accountsApi: AccountsApi,
    private val retrofitConfig: RetrofitConfig,
) : AccountsSource, BaseRetrofitSource(retrofitConfig) {

    override suspend fun getAccountBySessionId(sessionId: String): AccountEntity = wrapRetrofitExceptions {
        val account = accountsApi.getAccountBySessionId(sessionId)
        account.toEntity()
    }

    override suspend fun createRequestToken(): String = wrapRetrofitExceptions {
        val createRequestTokenResponse = accountsApi.createRequestToken()
        createRequestTokenResponse.requestToken
    }

    override suspend fun createSessionByUsernameAndPassword(
        username: String,
        password: String,
        requestToken: String,
    ): String = wrapRetrofitExceptions {
        val response = accountsApi.signInByUsernameAndPassword(
            username = username,
            password = password,
            requestToken = requestToken,
        )
        response.requestToken
    }

    override suspend fun createSession(requestToken: String): String = wrapRetrofitExceptions {
        val response = accountsApi.createSession(requestToken)
        response.sessionId
    }

}