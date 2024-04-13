package com.ru.movieshows.data.accounts

import com.ru.movieshows.core.NotAuthException
import com.ru.movieshows.core.Container
import com.ru.movieshows.core.flow.LazyFlowSubjectFactory
import com.ru.movieshows.data.AccountsDataRepository
import com.ru.movieshows.data.accounts.models.AccountDataModel
import com.ru.movieshows.data.accounts.sources.AccountsDataSource
import com.ru.movieshows.data.settings.sources.SettingsDataSource
import com.ru.movieshows.data.settings.sources.SharedContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountsRetrofitSourceImpl @Inject constructor(
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
                .collect { container ->
                    if (container is SharedContainer.Value) {
                        if (container.value != null) {
                            accountLazyFlowSubject.newAsyncLoad(silently = true)
                        } else {
                            accountLazyFlowSubject.updateWith(Container.Error(NotAuthException()))
                        }
                    }
                }
        }
    }

    override fun getAccount(): Flow<Container<AccountDataModel>> {
        return accountLazyFlowSubject.listen()
    }

    override suspend fun isSignedIn(): Boolean {
        val lastValue = accountLazyFlowSubject.getLastValue()
        return lastValue is Container.Success<AccountDataModel>
    }

    override suspend fun signIn(email: String, password: String): String {
        val requestToken = accountsDataSource.createRequestToken()
        val sessionRequestToken = accountsDataSource.createSessionByUsernameAndPassword(
            email,
            password,
            requestToken,
        )
        return accountsDataSource.createSession(sessionRequestToken)
    }

    override suspend fun logout() {
        settingsDataSource.setCurrentSessionId(null)
    }

    override fun reload() {
        accountLazyFlowSubject.newAsyncLoad()
    }

}