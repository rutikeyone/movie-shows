package com.ru.movieshows.app.model.accounts

import arrow.core.Either
import com.ru.movieshows.app.model.AppFailure
import com.ru.movieshows.app.model.Empty
import com.ru.movieshows.app.model.Error
import com.ru.movieshows.app.model.Pending
import com.ru.movieshows.app.model.Success
import com.ru.movieshows.app.model.settings.AppSettingsRepository
import com.ru.movieshows.app.utils.async.LazyFlowSubject
import com.ru.movieshows.sources.accounts.entities.AccountEntity
import com.ru.movieshows.sources.accounts.entities.UserAuthenticationState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class AccountsRepositoryImpl @Inject constructor(
    private val accountsSource: AccountsSource,
    private val appSettingsRepository: AppSettingsRepository,
    ) : AccountRepository  {

    private val accountLazyFlowSubject = LazyFlowSubject<Unit, UserAuthenticationState> {
        loadUserAuthenticationState()
    }

    override suspend fun getUserAuthenticationState(): Flow<UserAuthenticationState> {
        return accountLazyFlowSubject.listen(Unit).flatMapLatest { result ->
            val state = when(result) {
                is Empty -> UserAuthenticationState.Empty
                is Pending -> UserAuthenticationState.Pending(isPreviousNotAuthenticatedState())
                is Success -> result.value
                is Error -> UserAuthenticationState.NotAuthentication(isFirstLaunch(), isPreviousAuthenticatedState())
            }
            flowOf(state)
        }
    }

    override suspend fun isSignedIn(): Boolean {
        val lastValue = lastValue()
        return lastValue is UserAuthenticationState.Authentication
    }

    override suspend fun signIn(email: String, password: String): Either<AppFailure, String> {
        return try {
            val requestToken = accountsSource.createRequestToken()
            val sessionRequestToken = accountsSource.createSessionByUsernameAndPassword(
                username = email,
                password = password,
                requestToken = requestToken,
            )
            val sessionId = accountsSource.createSession(sessionRequestToken)
            appSettingsRepository.setCurrentSessionId(sessionId)
            accountLazyFlowSubject.updateWithArgument(Unit) {
                loadUserAuthenticationState()
            }
            Either.Right(sessionId)
        } catch (e: AppFailure) {
            Either.Left(e)
        } catch (e: Exception) {
            Either.Left(AppFailure.Empty)
        }
    }

    override suspend fun logout() {
        val notAuthenticationState = UserAuthenticationState.NotAuthentication(
            isFirstLaunch(),
            isLastValueAuthenticatedState()
        )
        appSettingsRepository.cleanCurrentSessionId()
        accountLazyFlowSubject.updateAllValues(notAuthenticationState)
    }

    override suspend fun getAccountBySessionId(sessionId: String): AccountEntity {
        return accountsSource.getAccountBySessionId(sessionId)
    }

    private suspend fun loadUserAuthenticationState(): UserAuthenticationState {
        return try {
            val sessionId = appSettingsRepository.getCurrentSessionId()
            if (sessionId != null) {
                val account = getAccountBySessionId(sessionId)
                UserAuthenticationState.Authentication(isFirstLaunch(), account)
            } else {
                UserAuthenticationState.NotAuthentication(isFirstLaunch(), isPreviousAuthenticatedState())
            }
        } catch (e: AppFailure) {
            appSettingsRepository.cleanCurrentSessionId()
            UserAuthenticationState.NotAuthentication(
                firstLaunch = isFirstLaunch(),
                previousAuthenticated = isPreviousAuthenticatedState(),
                error = e,
            )
        } catch (e: Exception) {
            appSettingsRepository.cleanCurrentSessionId()
            UserAuthenticationState.NotAuthentication(
                firstLaunch = isFirstLaunch(),
                previousAuthenticated = isPreviousAuthenticatedState(),
                error = AppFailure.Empty,
            )
        }
    }

    private fun previousValue(): UserAuthenticationState {
        val futureRecord = accountLazyFlowSubject.futureRecord(Unit)
        val previousValue = futureRecord?.previousValue?.getValueOrNull()
        return previousValue ?: UserAuthenticationState.Empty
    }

    private fun lastValue(): UserAuthenticationState {
        val futureRecord = accountLazyFlowSubject.futureRecord(Unit)
        val lastValue = futureRecord?.lastValue?.getValueOrNull()
        return lastValue ?: UserAuthenticationState.Empty
    }

    private fun isPreviousAuthenticatedState(): Boolean {
        val previousValue = previousValue()
        return previousValue is UserAuthenticationState.Authentication
    }

    private fun isPreviousNotAuthenticatedState(): Boolean {
        val previousValue = previousValue()
        return previousValue is UserAuthenticationState.NotAuthentication
    }

    private fun isLastValueAuthenticatedState(): Boolean {
        val lastValue = lastValue()
        return lastValue is UserAuthenticationState.Authentication
    }

    private fun isFirstLaunch(): Boolean {
        return appSettingsRepository.getIsFirstLaunch()
    }
}