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
import com.ru.movieshows.sources.accounts.entities.AuthStateEntity
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

    private val isFirstLaunch = appSettingsRepository.getIsFirstLaunch()

    private val accountLazyFlowSubject = LazyFlowSubject<Unit, AuthStateEntity> {
        getAuthState()
    }

    private fun previousValue(): AuthStateEntity {
        val previousValue = accountLazyFlowSubject.futureRecord(Unit)?.previousValue?.getValueOrNull()
        return previousValue ?: AuthStateEntity.Empty
    }

    private fun lastValue(): AuthStateEntity {
        val lastValue = accountLazyFlowSubject.futureRecord(Unit)?.lastValue?.getValueOrNull()
        return lastValue ?: AuthStateEntity.Empty
    }

    private fun isPreviousAuthState() = previousValue() is AuthStateEntity.Auth

    private fun isPreviousNotAuthState() = previousValue() is AuthStateEntity.NotAuth

    private fun isLastValueAuthState() = lastValue() is AuthStateEntity.Auth

    override suspend fun getState(): Flow<AuthStateEntity> {
        return accountLazyFlowSubject.listen(Unit).flatMapLatest{ result ->
            val state = when(result) {
                is Empty -> AuthStateEntity.Empty
                is Pending -> AuthStateEntity.Pending(isPreviousNotAuthState())
                is Success -> result.value
                is Error -> AuthStateEntity.NotAuth(isFirstLaunch, isPreviousAuthState())
            }
            flowOf(state)
        }
    }

    override suspend fun isSignedIn(): Boolean = lastValue() is AuthStateEntity.Auth

    override suspend fun signIn(email: String, password: String): Either<AppFailure, String> {
        return try {
            val requestToken = accountsSource.createRequestToken()
            val sessionRequestToken = accountsSource.createSessionByUsernameAndPassword(email, password, requestToken)
            val sessionId = accountsSource.createSession(sessionRequestToken)
            appSettingsRepository.setCurrentSessionId(sessionId)
            accountLazyFlowSubject.updateWithArgument(Unit) {
                getAuthState()
            }
            Either.Right(sessionId)
        } catch (e: AppFailure) {
            Either.Left(e)
        } catch (e: Exception) {
            Either.Left(AppFailure.Empty)
        }
    }

    override suspend fun logout() {
        val notAuthState = AuthStateEntity.NotAuth(isFirstLaunch, isLastValueAuthState())
        appSettingsRepository.cleanCurrentSessionId()
        accountLazyFlowSubject.updateAllValues(notAuthState)
    }

    override suspend fun getAccountBySessionId(sessionId: String): AccountEntity {
        return accountsSource.getAccountBySessionId(sessionId)
    }

    private suspend fun getAuthState(): AuthStateEntity {
        return try {
            val sessionId = appSettingsRepository.getCurrentSessionId()
            if (sessionId != null) {
                val account = getAccountBySessionId(sessionId)
                AuthStateEntity.Auth(isFirstLaunch, account)
            } else {
                AuthStateEntity.NotAuth(isFirstLaunch, isPreviousAuthState())
            }
        } catch (e: AppFailure) {
            appSettingsRepository.cleanCurrentSessionId()
            AuthStateEntity.NotAuth(isFirstLaunch, isPreviousAuthState(), e)
        } catch (e: Exception) {
            appSettingsRepository.cleanCurrentSessionId()
            AuthStateEntity.NotAuth(isFirstLaunch, isPreviousAuthState(), AppFailure.Empty)
        }
    }

}