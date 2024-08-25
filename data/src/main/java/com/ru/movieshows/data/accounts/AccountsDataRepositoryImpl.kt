package com.ru.movieshows.data.accounts

import com.ru.movieshows.core.NotAuthException
import com.ru.movieshows.core.Container
import com.ru.movieshows.core.flow.LazyFlowSubjectFactory
import com.ru.movieshows.data.AccountsDataRepository
import com.ru.movieshows.data.IODispatcher
import com.ru.movieshows.data.accounts.models.AccountDataModel
import com.ru.movieshows.data.accounts.sources.AccountsDataSource
import com.ru.movieshows.data.settings.sources.SettingsDataSource
import com.ru.movieshows.data.settings.sources.SharedContainer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountsDataRepositoryImpl @Inject constructor(
    @IODispatcher private val dispatcher: CoroutineDispatcher,
    private val accountsDataSource: AccountsDataSource,
    private val settingsDataSource: SettingsDataSource,
    coroutineScope: CoroutineScope,
    lazyFlowSubjectFactory: LazyFlowSubjectFactory,
) : AccountsDataRepository {

    private val accountLazyFlowSubject = lazyFlowSubjectFactory.create {
        accountsDataSource.getAccount()
    }

    init {
        coroutineScope.launch {
            settingsDataSource.listenCurrentSessionId()
                .flowOn(dispatcher)
                .collect { container ->
                    if (container is SharedContainer.Value) {
                        if (container.value != null) {
                            accountLazyFlowSubject.newAsyncLoad()
                        } else {
                            accountLazyFlowSubject.updateWith(Container.Error(NotAuthException()))
                        }
                    }
                }
        }
    }

    override fun getAccount(): Flow<Container<AccountDataModel>> {
        return accountLazyFlowSubject
            .listen()
            .flowOn(dispatcher)
    }

    override suspend fun isSignedIn(): Boolean {
        val lastValue = withContext(dispatcher) {
            accountLazyFlowSubject.getLastValue()
        }

        return lastValue is Container.Success<AccountDataModel>
    }

    override suspend fun signIn(username: String, password: String): String {
        val sessionId = withContext(dispatcher) {
            val requestToken = accountsDataSource.createRequestToken()
            val sessionRequestToken = accountsDataSource.createSessionByUsernameAndPassword(
                username = username,
                password = password,
                requestToken = requestToken,
            )
            val sessionId = accountsDataSource.createSession(sessionRequestToken)
            sessionId
        }

        return sessionId
    }

    override fun logout() {
        settingsDataSource.setCurrentSessionId(null)
    }

    override fun reload() {
        accountLazyFlowSubject.newAsyncLoad()
    }

}